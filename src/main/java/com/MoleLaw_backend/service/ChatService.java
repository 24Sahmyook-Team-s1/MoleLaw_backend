package com.MoleLaw_backend.service;

import com.MoleLaw_backend.domain.entity.ChatRoom;
import com.MoleLaw_backend.domain.entity.Message;
import com.MoleLaw_backend.domain.entity.User;
import com.MoleLaw_backend.domain.repository.ChatRoomRepository;
import com.MoleLaw_backend.domain.repository.MessageRepository;
import com.MoleLaw_backend.dto.*;
import com.MoleLaw_backend.dto.response.AnswerResponse;
import com.MoleLaw_backend.service.law.FinalAnswer;
import com.MoleLaw_backend.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final FinalAnswer finalAnswer;

    // 🔸 채팅방 생성
    public ChatRoomResponse createChatRoom(User user, ChatRoomRequest request) {
        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .title(request.getTitle())
                .build();
        return ChatRoomResponse.from(chatRoomRepository.save(chatRoom));
    }

    // 🔸 사용자 채팅방 전체 목록 (첫 메시지 복호화 포함)
    public List<ChatRoomResponse> getMyChatRooms(User user) {
        return chatRoomRepository.findByUser(user).stream()
                .map(room -> {
                    Message firstMessage = messageRepository
                            .findFirstByChatRoomIdOrderByTimestampAsc(room.getId())
                            .orElse(null);

                    String preview = firstMessage != null
                            ? EncryptUtil.decrypt(firstMessage.getContent())
                            : "(메시지 없음)";

                    return ChatRoomResponse.from(room, preview);
                })
                .collect(Collectors.toList());
    }

    // 🔸 특정 채팅방의 메시지 목록 (복호화 포함)
    public List<MessageResponse> getMessages(Long chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomId)
                .stream()
                .map(MessageResponse::from)
                .collect(Collectors.toList());
    }

    // 🔸 기존 채팅방에서 질문 → GPT 답변 → 저장
    public void askQuestion(User user, Long chatRoomId, MessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        messageRepository.save(Message.builder()
                .chatRoom(room)
                .sender(Message.Sender.USER)
                .content(EncryptUtil.encrypt(request.getContent()))
                .build());

        AnswerResponse answer = finalAnswer.getAnswer(request.getContent());

        messageRepository.save(Message.builder()
                .chatRoom(room)
                .sender(Message.Sender.BOT)
                .content(EncryptUtil.encrypt(answer.getAnswer()))
                .build());
    }

    // 🔸 새로운 채팅방 생성 + 질문/응답 저장
    public List<MessageResponse> createRoomAndAsk(User user, FirstMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .title(request.getTitle() != null ? request.getTitle() : "새로운 상담")
                .build());

        messageRepository.save(Message.builder()
                .chatRoom(chatRoom)
                .sender(Message.Sender.USER)
                .content(EncryptUtil.encrypt(request.getContent()))
                .build());

        AnswerResponse answer = finalAnswer.getAnswer(request.getContent());

        messageRepository.save(Message.builder()
                .chatRoom(chatRoom)
                .sender(Message.Sender.BOT)
                .content(EncryptUtil.encrypt(answer.getAnswer()))
                .build());

        return getMessages(chatRoom.getId());
    }

    // 🔸 프론트에서 배열로 보내는 메시지들 bulk 저장
    public void saveBulkMessages(User user, BulkChatSaveRequest request) {
        ChatRoom room = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .title(request.getTitle())
                .build());

        for (BulkChatSaveRequest.BulkMessage m : request.getMessages()) {
            messageRepository.save(Message.builder()
                    .chatRoom(room)
                    .sender(Message.Sender.valueOf(m.getSender()))
                    .content(EncryptUtil.encrypt(m.getContent()))
                    .build());
        }
    }
}
