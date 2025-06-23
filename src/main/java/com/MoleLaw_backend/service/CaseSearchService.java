package com.MoleLaw_backend.service;

import com.MoleLaw_backend.dto.PrecedentInfo;
import java.util.List;

public interface CaseSearchService {
    List<PrecedentInfo> searchCases(String query);
}
