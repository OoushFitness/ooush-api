package com.ooush.api.service.bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ooush.api.dto.response.BitmapSearchOption;
import com.ooush.api.entity.ExerciseBitmapPosition;
import com.ooush.api.repository.ExerciseBitmapPositionRepository;

@Service
@Transactional
public class BitmapServiceImpl implements BitmapService {

    @Autowired
    ExerciseBitmapPositionRepository exerciseBitmapPositionRepository;

    @Override
    public Map<String, List<BitmapSearchOption>> fetchSearchOptions() {
        Map<String, List<BitmapSearchOption>> bitmapSearchOptionsResponse = new HashMap<>();
        List<ExerciseBitmapPosition> bitmapPositions = exerciseBitmapPositionRepository.findAll();
        List<String> uniqueSearchTypes = bitmapPositions.stream().map(ExerciseBitmapPosition::getType).distinct().collect(
                Collectors.toList());
        for (String searchType : uniqueSearchTypes) {
            bitmapSearchOptionsResponse.put(searchType, new ArrayList<>());
        }
        for (ExerciseBitmapPosition bitmapPosition : bitmapPositions) {
            bitmapSearchOptionsResponse.get(bitmapPosition.getType()).add(new BitmapSearchOption(
                    bitmapPosition.getName(),
                    bitmapPosition.getPosition()
            ));
        }
        return bitmapSearchOptionsResponse;
    }
}
