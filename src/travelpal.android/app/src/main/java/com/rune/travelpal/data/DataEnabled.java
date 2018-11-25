package com.rune.travelpal.data;

import com.rune.travelpal.data.remote.RemoteDataGateway;

public interface DataEnabled {

    DataGateway getDataGateway();

    RemoteDataGateway getRemoteDataGateway();
}
