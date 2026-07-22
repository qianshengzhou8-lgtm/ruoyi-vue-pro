package cn.iocoder.yudao.module.school.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.event.ExcelReadListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
class TestData {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;
}

public class EasyExcelTest {
    public static void main(String[] args) {
        System.out.println("EasyExcel test - package available");
    }
}