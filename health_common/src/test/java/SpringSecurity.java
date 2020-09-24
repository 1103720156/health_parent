import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author 意风秋
 * @Date 2020/09/22 22:17
 **/
public class SpringSecurity {
    public static void main(String[] args) {
         BCryptPasswordEncoder passwordEncoder = null;

         System.out.println(passwordEncoder.encode("admin"));

    }
}
