package com.itheIma.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheIma.dao.MemberDao;
import com.itheIma.pojo.Member;
import com.itheIma.service.MemberService;
import com.itheIma.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 意风秋
 * @Date 2020/09/23 15:47
 **/
@Service( interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 月份查询会员数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list=new ArrayList<>();
        for(String str : months){
            str=str + ".31";
            list.add(memberDao.findMemberCountBeforeDate(str));
        }
        return list;
    }

    @Override
    public void add(Member member) {
        //如果用户设置了密码，需要对密码进行md5加密
        String password = member.getPassword();
        if(password != null){
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }
}
