package com.itheIma.service;

import com.itheIma.pojo.Member;

import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/09/22 23:58
 **/
public interface MemberService {
    /**
     * 月份查询会员统计
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> months);

    /**
     * 增加会员
     * @param member
     */
    void add(Member member);

    /**
     * 查看是否是会员
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);
}
