# MentalApp ソースコード構成ガイド

## 目次

1. [概要](#概要)
2. [controllerパッケージ](#controllerパッケージ)
3. [entityパッケージ](#entityパッケージ)
4. [mapperパッケージ](#mapperパッケージ)
5. [securityパッケージ](#securityパッケージ)
6. [serviceパッケージ](#serviceパッケージ)
7. [userパッケージ](#userパッケージ)

## 概要

このドキュメントでは、MentalAppアプリケーションのソースコード構成について説明します。アプリケーションは主に以下のパッケージで構成されています：

- `controller`: ユーザーリクエストを処理するコントローラークラス
- `entity`: データベースエンティティを表すクラス
- `mapper`: MyBatisのデータアクセスインターフェース
- `security`: Spring Securityの設定とカスタマイズ
- `service`: ビジネスロジックを実装するサービスクラス
- `user`: ユーザー関連のDTOクラス

## controllerパッケージ

このパッケージには、ユーザーリクエストを処理するコントローラークラスが含まれています。

### DemoController

```java
@Controller
public class DemoController {
    @GetMapping("/")
    public String showHome() {
        return "home";
    }
    
    @GetMapping("/leaders")
    public String showLeaders() {
        return "leaders";
    }
    
    @GetMapping("/systems")
    public String showSystems() {
        return "systems";
    }
}
```

**役割**: 
- ホームページ、リーダーページ、システムページへのリクエストを処理
- 対応するビューテンプレートを返す

### LoginController

```java
@Controller
public class LoginController {
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "fancy-login";
    }
    
    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}
```

**役割**:
- ログインページと権限拒否ページへのリクエストを処理
- 対応するビューテンプレートを返す

### RegistrationController02

```java
@Controller
@RequestMapping("/register")
public class RegistrationController02 {
    private UserService02Impl userService;
    
    // 登録フォーム表示
    @GetMapping("/showRegistrationForm02")
    public String showMyLoginPage(Model theModel) {
        theModel.addAttribute("webUser", new WebUser());
        return "register/registration-form02";
    }
    
    // 登録処理
    @PostMapping("/processRegistrationForm02")
    public String processRegistrationForm(
            @Valid @ModelAttribute("webUser") WebUser theWebUser,
            BindingResult theBindingResult,
            HttpSession session, Model theModel) {
        // バリデーション、ユーザー重複チェック、保存処理
        // ...
    }
}
```

**役割**:
- ユーザー登録フォームの表示と処理
- 入力バリデーション
- ユーザー名の重複チェック
- 新規ユーザーの保存

## entityパッケージ

このパッケージには、データベースのテーブルに対応するエンティティクラスが含まれています。

### User

```java
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username")
    private String userName;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "enabled")
    private boolean enabled;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;
    
    // コンストラクタ、ゲッター、セッター
}
```

**役割**:
- `user`テーブルのデータをマッピング
- ユーザー情報（ID、ユーザー名、パスワード、有効フラグ、名前、メール）を保持
- ロールとの多対多関連を管理

### Role

```java
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    // コンストラクタ、ゲッター、セッター
}
```

**役割**:
- `role`テーブルのデータをマッピング
- ロール情報（ID、ロール名）を保持

## mapperパッケージ

このパッケージには、MyBatisのデータアクセスインターフェースが含まれています。

### UserMapper

```java
@Mapper
public interface UserMapper {
    // 全件取得
    List<User> selectAll();
    
    // １件取得
    User selectByPrimaryKey(String userName);
    
    // 登録
    int insert(User user);
    
    // 更新
    int updateByPrimaryKey(User user);
    
    // 削除
    int deleteByPrimaryKey(Long id);
}
```

**役割**:
- ユーザーデータのCRUD操作を提供
- SQLの実行はXMLマッピングファイルで定義

## securityパッケージ

このパッケージには、Spring Securityの設定とカスタマイズクラスが含まれています。

### CustomAuthenticationSuccessHandler

```java
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private UserService02 userService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String userName = authentication.getName();
        User theUser = userService.findByUserName(userName);
        
        // セッションにユーザー情報を保存
        HttpSession session = request.getSession();
        session.setAttribute("user", theUser);
        
        // ホームページにリダイレクト
        response.sendRedirect(request.getContextPath() + "/");
    }
}
```

**役割**:
- 認証成功時の処理をカスタマイズ
- ユーザー情報をセッションに保存
- ホームページにリダイレクト

### DemoSecurityConfig

```java
@Configuration
public class DemoSecurityConfig {
    // パスワードエンコーダーの定義
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 認証プロバイダーの定義
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService02 userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    // セキュリティフィルターチェーンの定義
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        // TODO ROLEの実装
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
        
        return http.build();
    }
}
```

**役割**:
- Spring Securityの設定
- パスワードエンコーダーの定義
- 認証プロバイダーの設定
- セキュリティフィルターチェーンの構成
- URL認可ルールの定義
- ログインページとログアウト処理の設定
- アクセス拒否ページの設定

## serviceパッケージ

このパッケージには、ビジネスロジックを実装するサービスクラスが含まれています。

### UserService02

```java
public interface UserService02 extends UserDetailsService {
    public User findByUserName(String userName);
    void save(WebUser webUser);
}
```

**役割**:
- ユーザー関連のサービスインターフェースを定義
- Spring SecurityのUserDetailsServiceを拡張

### UserService02Impl

```java
@Service
@Transactional
public class UserService02Impl implements UserService02 {
    private final UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    
    // ユーザー名でユーザーを検索
    @Override
    public User findByUserName(String userName) {
        return userMapper.selectByPrimaryKey(userName);
    }
    
    // WebUserからUserを作成して保存
    @Override
    public void save(WebUser webUser) {
        User user = new User();
        user.setUserName(webUser.getUserName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setFirstName(webUser.getFirstName());
        user.setLastName(webUser.getLastName());
        user.setEmail(webUser.getEmail());
        user.setEnabled(true);
        
        userMapper.insert(user);
    }
    
    // Spring SecurityのUserDetailsServiceの実装
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.selectByPrimaryKey(userName);
        
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        
        // TODO ROLEの実装要否検討
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(), 
                user.getPassword(),
                new ArrayList<>());
    }
}
```

**役割**:
- ユーザー関連のビジネスロジックを実装
- ユーザー検索機能の提供
- 新規ユーザー登録処理
- Spring Securityの認証処理

## userパッケージ

このパッケージには、ユーザー関連のDTOクラスが含まれています。

### WebUser

```java
public class WebUser {
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String userName;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String password;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    @Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    
    // コンストラクタ、ゲッター、セッター
}
```

**役割**:
- ユーザー登録フォームからのデータを保持するDTOクラス
- バリデーション用のアノテーションを提供
- フォームデータとエンティティ間の変換を支援