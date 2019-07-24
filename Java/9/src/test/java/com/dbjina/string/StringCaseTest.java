// todo JSONString parse 라이브러리 별로 테스트 해보기.
//   라이브러리 별로 상황에 따라서 속도가 다름. 가능하면 JSONString 케이스를 판별해서 라이브러리를 골라 쓰면 어떨가 생각중.
//   https://blog.overops.com/the-ultimate-json-library-json-simple-vs-gson-vs-jackson-vs-json/

package com.dbjina.string;

import com.google.i18n.phonenumbers.NumberParseException;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringCaseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("JSONString to Map 테스트 : 기본")
    @ParameterizedTest
    @ValueSource(strings = {
              "{'name'='Kim','salary'=10000}"
            , "{name:Kim,'salary':10000}"
            , "{name:Kim,'salary':10000}"
            , "{name:Kim,salary:10000}"
    })
    void convertStringToMap1(String str) {
        Map<String, Object> map = StringCase.convertStringToMap(str);
        
        assertNotNull(map);
    }

    @DisplayName("JSONString to List 테스트 : 기본")
    @ParameterizedTest
    @ValueSource(strings = {
              "[{'name'='Kim','salary'=10000}]"
            , "[{name:Kim,'salary':10000}]"
            , "[{name:Kim,'salary':10000}]"
            , "[{name:Kim,salary:10000}]"  
    })
    void convertStringToList1(String str) {
        List<Map<String, Object>> list = StringCase.convertStringToList(str);

        assertNotNull(list);
        assertThat("사이즈가 1개 이어야 함"
                , list.size()
                , equalTo(1)
        );
    }

    @DisplayName("JSONString to List 테스트 : 사이즈가 2일 경우")
    @ParameterizedTest
    @ValueSource(strings = {
              "[{'name'='Kim','salary'=10000}   ,{'name'='Lee','salary'=8000}]"
            , "[{name:Kim,'salary':10000}       ,{name:Lee,'salary':8000}]"
            , "[{name:Kim,'salary':10000}       ,{name:Lee,'salary':8000}]"
            , "[{name:Kim,salary:10000}         ,{name:Lee,salary:8000}]"
    })
    void convertStringToList2(String str) {
        List<Map<String, Object>> list = StringCase.convertStringToList(str);

        assertNotNull(list);
        assertThat("사이즈가 2이어야 함"
                , list.size()
                , equalTo(2)
        );
        assertEquals("Kim", list.get(0).get("name"));
        
        // longValue() 를 하든, doubleValue()를 하든 그에 따라서 assertEquals 가 expected 타입을 바꺼서 의미가 없는듯?
        // 그래서 아래의 assertThat 을 이용해서 테스트 하는게 정확한듯.
        assertEquals(10000, ((Number)list.get(0).get("salary")).doubleValue() );
        
        assertThat("타입 테스트"
            , ((Number)list.get(0).get("salary")).intValue()
            , equalTo(10000)
        );
    }

    @DisplayName("JSONString to DateTime 테스트 : 기본")
    @ParameterizedTest
    @ValueSource(strings = {
              "20190721000000"
            , "2019072100000"
            , "201907210000"
            , "20190721000"
            , "2019072100"
            , "20190721"
            , "2019-07-02"
            , "2019-07-02 16"
            , "2019-07-02 16:43"
            , "2019-07-02 16:43:00"
            , "2019/07/02"
            , "2019/07/02 16"
            , "2019/07/02 16:43"
            , "2019/07/02 16:43:00"
            , "2019.07.02"
            , "2019.07.02 16"
            , "2019.07.02 16:43"
            , "2019.07.02 16:43:00"
    })
    void convertStringToDateTime1(String str) {
        String pattern = "yyyyMMddHHmmss";
        DateTime dateTime = StringCase.convertStringToDateTime(str);
        
        assertNotNull(dateTime);
        
        String expectedDateStr = dateTime.toString(pattern);
        
        assertEquals(expectedDateStr, dateTime.toString(pattern));
    }


    @DisplayName("전화번호 to 한국 전화번호 표시 테스트 : 기본")
    @ParameterizedTest
    @ValueSource(strings = {
            "01012341234"
            , "0171231234"
            , "0111231234"
            , "021231234"
            , "0212341234"
            , "0511231234"
            , "16881234"
            , "010-1234-1234"
            , "017-123-1234"
            , "011-123-1234"
            , "02-123-1234"
            , "02-1234-1234"
            , "051-123-1234"
            , "1688-1234"
            , "010 1234 1234"
            , "017 123 1234"
            , "011 123 1234"
            , "02 123 1234"
            , "02 1234 1234"
            , "051 123 1234"
            , "1688 1234"
            , "+82 10-1234-1234"
            , "+82 17-123-1234"
            , "+82 11-123-1234"
            , "+82 2-123-1234"
            , "+82 2-1234-1234"
            , "+82 51-123-1234"
            , "+82 010-1234-1234"
            , "+82 017-123-1234"
            , "+82 011-123-1234"
            , "+82 02-123-1234"
            , "+82 02-1234-1234"
            , "+82 051-123-1234"
    })
    void parsePhoneNumber1(String str) throws NumberParseException {
        String number = StringCase.parsePhoneNumber(str);

        assertNotNull(number);

        assertNotEquals("", number);
    }

    @DisplayName("전화번호 to 한국 전화번호 표시 테스트 : Throw NumberParseException")
    @ParameterizedTest
    @ValueSource(strings = {
            ""
            , "abcdefghijk"
            , "0!0!234!234"
            , "공일공일이삼사일이삼사"
    })
    void parsePhoneNumber2(String str) throws NumberParseException {
        assertThrows(NumberParseException.class, () -> {
            System.out.println(StringCase.parsePhoneNumber(str));
        });
    }
}
