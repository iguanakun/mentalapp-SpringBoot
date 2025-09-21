package com.mentalapp.common.service;

// import com.demosecurity.common.dao.RoleDao;
import com.mentalapp.common.dao.UserMapper;
import com.mentalapp.common.entity.Role;
import com.mentalapp.common.entity.User;
import com.mentalapp.common.user.WebUser;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** ユーザーサービスの実装クラス ユーザーの認証、登録、検索などの機能を提供します */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  // private RoleDao roleDao;

  /**
   * ユーザー名によるユーザー検索
   *
   * @param userName 検索するユーザー名
   * @return 見つかったユーザー、存在しない場合はnull
   */
  @Override
  public User findByUserName(String userName) {
    // check the database if the user already exists
    return userMapper.selectByPrimaryKey(userName);
  }

  /**
   * 新規ユーザーの保存
   *
   * @param webUser 保存するユーザー情報
   */
  @Override
  public void save(WebUser webUser) {
    User user = new User();

    // assign user details to the user object
    user.setUserName(webUser.getUserName());
    user.setPassword(passwordEncoder.encode(webUser.getPassword()));
    user.setFirstName(webUser.getFirstName());
    user.setLastName(webUser.getLastName());
    user.setEmail(webUser.getEmail());
    user.setEnabled(true);

    // give user default role of "employee"
    // user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));

    // save user in the database
    userMapper.insert(user);
  }

  /**
   * Spring Securityの認証のためのユーザー詳細を読み込み
   *
   * @param userName 認証するユーザー名
   * @return UserDetailsオブジェクト
   * @throws UsernameNotFoundException ユーザーが見つからない場合
   */
  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userMapper.selectByPrimaryKey(userName);

    if (user == null) {
      throw new UsernameNotFoundException("Invalid username or password.");
    }

    // TODO ROLEの実装要否検討
    // Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

    // return new org.springframework.security.core.userdetails.User(user.getUserName(),
    // user.getPassword(),
    // authorities);
    return new org.springframework.security.core.userdetails.User(
        user.getUserName(), user.getPassword(), new ArrayList<>());
  }

  /**
   * ロールをSpring Securityの権限に変換
   *
   * @param roles ユーザーのロールコレクション
   * @return 権限のコレクション
   */
  private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    for (Role tempRole : roles) {
      SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
      authorities.add(tempAuthority);
    }

    return authorities;
  }
}
