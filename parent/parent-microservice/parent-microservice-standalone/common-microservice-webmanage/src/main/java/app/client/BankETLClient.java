package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.bankETL.WebDataDebitcard;

/**
 *
 */
//@FeignClient("mobile-etl")
@FeignClient(name = "BANK-ETL", url = "http://10.167.202.218:1256")
public interface BankETLClient {


    @GetMapping("/etl/bank/tasks/debitcard/detail")
    WebDataDebitcard bankInfo(@RequestParam("taskid") String taskid,@RequestParam("loginName") String loginName);
}