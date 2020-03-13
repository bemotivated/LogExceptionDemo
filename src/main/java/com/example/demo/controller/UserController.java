package com.example.demo.controller;

import com.example.demo.Entity.WEntityVO;
import com.example.demo.Exception.JacksonException;
import com.example.demo.Exception.NotFindException;
import com.example.demo.Operation;
import com.example.demo.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController{
    @Operation(value="查询")
    @GetMapping("/{id}")
    public Result<?> listFocus(@PathVariable("id")String id, HttpServletRequest request){
      /*  BlogResult blogResult=BlogResult.requestSuccess("ok");
        int i=1/0;
        return bloeResult;*/
      Result result=new Result();
      result.setCode(230);
      result.setMessage("失败了");
      try {
          //int i=5/0;
          int x=6/0;
      }catch (Exception e) {
          //throw new Exception("没有找到");
          //return null;
          //throw new SQLException("获取类实例失败");
          //无法进入这个分支
         throw new NotFindException("没有发现");
      }
      return result;
    }

    @Operation(value="更新")
    @PostMapping(value = "/testValidate")
    public Result<?> uploadwordEntityVO(@Valid @RequestBody WEntityVO wEntityVO, BindingResult errors)  {
        //记录JackSon的错误参数
        List<ObjectError> objectErrorList=errors.getAllErrors();
        String errormessage="";
        for (ObjectError objectError : objectErrorList) {
            errormessage=errormessage+objectError.getDefaultMessage();
        }
        if(objectErrorList.size()>0){
            throw new JacksonException(errormessage);
        }
        Result<?> result = new Result<>();
        return result;
    }
}