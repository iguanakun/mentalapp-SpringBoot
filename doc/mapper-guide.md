# MentalApp MyBatis Mapper ガイド

## 目次

1. [概要](#概要)
2. [UserMapper.xml](#usermapperxml)
3. [SQLマッピングの詳細](#sqlマッピングの詳細)
4. [MyBatisの設定](#mybatisの設定)
5. [トラブルシューティング](#トラブルシューティング)

## 概要

このドキュメントでは、MentalAppアプリケーションで使用されているMyBatisのマッパー設定について説明します。MyBatisは、SQLをJavaコードから分離し、XMLファイルで管理するためのフレームワークです。

MentalAppでは、`src/main/resources/mapper`ディレクトリにマッパーXMLファイルが配置されています。これらのファイルは、Javaインターフェースで定義されたメソッドとSQLステートメントをマッピングします。

## UserMapper.xml

`UserMapper.xml`は、`com.demosecurity.mapper.UserMapper`インターフェースのSQLマッピングを定義しています。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.demosecurity.mapper.UserMapper">

    <!-- １件取得 -->
    <select id="selectByPrimaryKey" resultType="User">
        select * from user
        where username = #{userName}
    </select>

    <!-- 登録 -->
    <insert id="insert">
        insert into user(username, password, enabled, first_name, last_name, email)
        values(#{userName},#{password}, #{enabled}, #{firstName}, #{lastName}, #{email})
    </insert>

    <!-- 更新 -->
    <update id="updateByPrimaryKey">
        update user
        set name = #{name}
        where id = #{id}
    </update>

    <!-- 削除 -->
    <delete id="deleteByPrimaryKey">
        delete from user
        where id = #{id}
    </delete>

</mapper>
```

### 重要な点

- `namespace`属性は、対応するJavaインターフェース（`com.demosecurity.mapper.UserMapper`）を指定しています。
- 各SQLステートメントは、インターフェースのメソッド名と一致するID属性を持っています。
- パラメータは`#{パラメータ名}`の形式で参照されます。

## SQLマッピングの詳細

### selectByPrimaryKey

```xml
<select id="selectByPrimaryKey" resultType="User">
    select * from user
    where username = #{userName}
</select>
```

- **目的**: ユーザー名でユーザーを検索する
- **パラメータ**: `userName` (String)
- **戻り値**: `User`オブジェクト
- **注意点**: 
  - `username`カラムと`userName`プロパティの名前の違いに注意
  - MyBatisの設定で`map-underscore-to-camel-case=true`が設定されているため、スネークケースとキャメルケースの変換が自動的に行われる

### insert

```xml
<insert id="insert">
    insert into user(username, password, enabled, first_name, last_name, email)
    values(#{userName},#{password}, #{enabled}, #{firstName}, #{lastName}, #{email})
</insert>
```

- **目的**: 新しいユーザーを登録する
- **パラメータ**: `User`オブジェクト
- **戻り値**: 挿入された行数（int）
- **注意点**:
  - カラム名とプロパティ名のマッピングに注意
  - 自動生成されるIDは明示的に指定されていない

### updateByPrimaryKey

```xml
<update id="updateByPrimaryKey">
    update user
    set name = #{name}
    where id = #{id}
</update>
```

- **目的**: ユーザー情報を更新する
- **パラメータ**: `User`オブジェクト
- **戻り値**: 更新された行数（int）
- **注意点**:
  - **現在の実装に問題があります**: `name`カラムは`user`テーブルに存在しません
  - 正しくは、更新対象のカラム（`username`, `password`, `enabled`, `first_name`, `last_name`, `email`）を指定する必要があります

### deleteByPrimaryKey

```xml
<delete id="deleteByPrimaryKey">
    delete from user
    where id = #{id}
</delete>
```

- **目的**: ユーザーを削除する
- **パラメータ**: `id` (Long)
- **戻り値**: 削除された行数（int）

## MyBatisの設定

MyBatisの設定は`application.properties`ファイルで行われています：

```properties
# MyBatis Properties
mybatis.mapper-locations=classpath*:/mapper/*.xml
mybatis.type-aliases-package=com.demosecurity.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

### 設定の説明

- `mybatis.mapper-locations`: マッパーXMLファイルの場所を指定
- `mybatis.type-aliases-package`: エンティティクラスのパッケージを指定（短い名前で参照できるようになる）
- `mybatis.configuration.map-underscore-to-camel-case`: データベースのスネークケース（例：`first_name`）とJavaのキャメルケース（例：`firstName`）の自動変換を有効化

## トラブルシューティング

### 一般的な問題と解決策

1. **マッピングエラー**:
   - 症状: `Invalid bound statement (not found)`エラー
   - 原因: インターフェースのメソッド名とXMLのID属性が一致していない
   - 解決策: 名前が完全に一致していることを確認する

2. **パラメータマッピングエラー**:
   - 症状: `Parameter not found`エラー
   - 原因: XMLで参照されているパラメータ名がJavaメソッドのパラメータ名と一致していない
   - 解決策: パラメータ名を一致させるか、`@Param`アノテーションを使用する

3. **カラム名とプロパティ名の不一致**:
   - 症状: データが正しくマッピングされない
   - 原因: データベースのカラム名とJavaのプロパティ名が一致していない
   - 解決策: 
     - `map-underscore-to-camel-case=true`を設定する
     - または、結果マッピングを明示的に定義する

### 現在の問題点

1. **updateByPrimaryKey**メソッドのSQL:
   - 問題: `name`カラムは`user`テーブルに存在しない
   - 解決策: 以下のように修正する必要があります：

```xml
<update id="updateByPrimaryKey">
    update user
    set username = #{userName},
        password = #{password},
        enabled = #{enabled},
        first_name = #{firstName},
        last_name = #{lastName},
        email = #{email}
    where id = #{id}
</update>
```

2. **selectAll**メソッドの実装:
   - 問題: インターフェースには`selectAll`メソッドが定義されていますが、XMLには対応するSQLがない
   - 解決策: 以下のSQLを追加する必要があります：

```xml
<select id="selectAll" resultType="User">
    select * from user
</select>
```