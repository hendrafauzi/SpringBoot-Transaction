package com.hendra.transaction.demo.dao;

import com.hendra.transaction.demo.exception.GeneralException;
import com.hendra.transaction.demo.mapper.BankAccountMapper;
import com.hendra.transaction.demo.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class BankAccountDAO extends JdbcDaoSupport
{
    @Autowired
    public BankAccountDAO(DataSource dataSource)
    {
        this.setDataSource(dataSource);
    }

    public int save(long id, String fullName, double balance)
    {
        String sql = "insert into bank_account(ID, Full_Name, Balance) values (?,?,?)";

        try {
            return this.getJdbcTemplate().update(sql, id, fullName, balance);
        }
        catch (Exception ex)
        {
            throw null;
        }
    }

    public List<BankAccountInfo> getBankAccounts()
    {
        String sql = BankAccountMapper.BASE_SQL;

        Object[] params = new Object[]{};
        BankAccountMapper mapper = new BankAccountMapper();

        return this.getJdbcTemplate().query(sql, params, mapper);
    }

    public BankAccountInfo findBankAccount(Long id)
    {
        String sql = BankAccountMapper.BASE_SQL + " where ba.Id = ? ";
        Object[] params = new Object[]{id};
        BankAccountMapper mapper = new BankAccountMapper();

        try {
            BankAccountInfo bankAccountInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
            return bankAccountInfo;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    //Transaction must be created before
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount(long id, double amount) throws GeneralException
    {
        BankAccountInfo accountInfo = this.findBankAccount(id);

        if(accountInfo == null)
        {
            throw new GeneralException("Account Not Found, Id: " + id);
        }

        double newBalance = accountInfo.getBalance() + amount;
        if(accountInfo.getBalance() + amount < 0)
        {
            throw new GeneralException("The money in the account " + id + ",  isn't enough (" + accountInfo.getBalance() + ")");
        }

        accountInfo.setBalance(newBalance);

        //Inject to DB
        String sqlQuery = "Update Bank_Account set Balance = ? where Id = ?";

        this.getJdbcTemplate().update(sqlQuery, accountInfo.getBalance(), accountInfo.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = GeneralException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, double amount) throws GeneralException
    {
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, -amount);
    }
}
