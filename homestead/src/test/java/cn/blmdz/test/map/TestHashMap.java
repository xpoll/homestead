package cn.blmdz.test.map;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class TestHashMap {
    
    public static void main(String[] args) {
        Map<AA, String> map1 = new HashMap<>();
        AA aa = new AA();
        aa.setAbc(213);
        
        AA bb = new AA();
        bb.setAbc(222);

        map1.put(aa, "aaa");
        map1.put(bb, "bbb");
        
        System.out.println(map1);
        
        Map<String, AA> map2 = new HashMap<>();
        AA cc = new AA();
        cc.setAbc(213);
        
        AA dd = new AA();
        dd.setAbc(222);

        map2.put("ddd", dd);
        map2.put("ccc", cc);
        
        System.out.println(map2);
        System.out.println(map2.values());
        
    }

    @Getter
    @Setter
    @ToString
    public static class AA {
        private Integer abc;

        @Override
        public int hashCode() {
            return 9;
//            final int prime = 31;
//            int result = 1;
//            result = prime * result + ((abc == null) ? 0 : abc.hashCode());
//            return result;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
//            if (this == obj)
//                return true;
//            if (obj == null)
//                return false;
//            if (getClass() != obj.getClass())
//                return false;
//            AA other = (AA) obj;
//            if (abc == null) {
//                if (other.abc != null)
//                    return false;
//            } else if (!abc.equals(other.abc))
//                return false;
//            return true;
        }
        
    }
}
