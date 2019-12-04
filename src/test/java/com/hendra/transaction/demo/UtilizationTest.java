package com.hendra.transaction.demo;

import com.hendra.transaction.demo.mapper.BankAccountMapper;
import org.junit.Test;

public class UtilizationTest
{
    @Test
    public void testString()
    {
//        StringBuilder x = new StringBuilder();
//        String z = " BBBB";
//        StringBuilder y = x.append("AAAA").append(z);

        StringBuilder sql = new StringBuilder();
        String findId = "where ba.Id = ?";
        StringBuilder query = sql.append(BankAccountMapper.BASE_SQL).append(findId);

        System.out.println(query);
    }
}
