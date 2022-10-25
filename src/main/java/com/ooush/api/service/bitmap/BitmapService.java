package com.ooush.api.service.bitmap;

import java.util.List;

import com.ooush.api.dto.response.BitmapSearchParameter;

public interface BitmapService {

    List<BitmapSearchParameter> fetchSearchOptions();

}
