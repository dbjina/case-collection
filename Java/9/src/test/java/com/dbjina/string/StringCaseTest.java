// todo JSONString parse 라이브러리 별로 테스트 해보기.
//   라이브러리 별로 상황에 따라서 속도가 다름. 가능하면 JSONString 케이스를 판별해서 라이브러리를 골라 쓰면 어떨가 생각중.
//   https://blog.overops.com/the-ultimate-json-library-json-simple-vs-gson-vs-jackson-vs-json/

package com.dbjina.string;

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

class StringCaseTest extends StringCase {

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
    void convertStringToMap1(String str) throws Exception {
        Map<String, Object> map = convertStringToMap(str);
        
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
    void convertStringToList1(String str) throws Exception {
        List<Map<String, Object>> list = convertStringToList(str);

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
    void convertStringToList2(String str) throws Exception {
        List<Map<String, Object>> list = convertStringToList(str);

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
}
