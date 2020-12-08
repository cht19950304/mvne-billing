package com.cmit.mvne.billing.user.analysis.service;

import com.cmit.mvne.billing.user.analysis.entity.SysRoamZoneGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Luxf
 * @since 2020-02-19
 */
public interface SysRoamZoneGroupService extends IService<SysRoamZoneGroup> {
    /*List<SysRoamZoneGroup> selectByCountryAndDate(int countryId);
    List<SysRoamZoneGroup> cache(Integer countryId, List<SysRoamZoneGroup> sysRoamZoneGroupList);*/

    List<SysRoamZoneGroup> selectByISO(String iso);
    List<SysRoamZoneGroup> cache(String iso, List<SysRoamZoneGroup> sysRoamZoneGroupList);
}
