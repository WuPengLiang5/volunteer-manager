package com.wpl.volunteer.service;

import com.wpl.volunteer.entity.Columns;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-14
 */
public interface IColumnsService extends IService<Columns> {
    List<Columns> getColumnsByPid(String pid);

    List<Columns> listWebColumns();
}
