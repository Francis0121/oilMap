package com.oilMap.server.user.fuel;

/**
 * Created by Francis on 2015-03-25.
 */
public interface UserFuelService {
    /**
     * 유류 정보 입력
     * @param userFuel
     *  userPn, displacement, cost, period
     */
    void insert(UserFuel userFuel);

    /**
     * 유류 정보 조회
     * @param userPn
     *  유저 고유 번호
     * @return
     */
    UserFuel selectOne(Integer userPn);

    /**
     * 유류 정보 업데이트
     * @param userFuel
     */
    void update(UserFuel userFuel);
}
