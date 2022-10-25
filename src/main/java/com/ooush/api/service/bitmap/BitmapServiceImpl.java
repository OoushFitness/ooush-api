package com.ooush.api.service.bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ooush.api.dto.response.BitmapSearchOption;
import com.ooush.api.dto.response.BitmapSearchParameter;
import com.ooush.api.entity.ExerciseBitmapPosition;
import com.ooush.api.repository.ExerciseBitmapPositionRepository;

@Service
@Transactional
public class BitmapServiceImpl implements BitmapService {

    @Autowired
    ExerciseBitmapPositionRepository exerciseBitmapPositionRepository;

    @Override
    public List<BitmapSearchParameter> fetchSearchOptions() {
        List<BitmapSearchParameter> bitmapSearchParameterResponse = new ArrayList<>();
        List<ExerciseBitmapPosition> bitmapPositions = exerciseBitmapPositionRepository.findAll();
        List<String> uniqueSearchTypes = bitmapPositions.stream().map(ExerciseBitmapPosition::getType).distinct().collect(
                Collectors.toList());
        for (String searchType : uniqueSearchTypes) {
            BitmapSearchParameter bitmapSearchParameter = new BitmapSearchParameter();
            bitmapSearchParameter.setSearchParameter(searchType);
            bitmapSearchParameterResponse.add(bitmapSearchParameter);
        }

        for (ExerciseBitmapPosition bitmapPosition : bitmapPositions) {
            for (BitmapSearchParameter bitmapSearchParameter : bitmapSearchParameterResponse) {
                BitmapSearchOption bitmapSearchOption = new BitmapSearchOption();
                if (bitmapPosition.getType().equals(bitmapSearchParameter.getSearchParameter())) {
                    bitmapSearchOption.setName(bitmapPosition.getName());
                    bitmapSearchOption.setPosition(bitmapPosition.getPosition());
                    bitmapSearchParameter.getSearchOptions().add(bitmapSearchOption);
                }
            }
        }
        return bitmapSearchParameterResponse;
    }
}
