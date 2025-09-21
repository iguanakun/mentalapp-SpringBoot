# メンタルヘルスアプリケーションER図

```mermaid
erDiagram
    USER {
        int id PK
        string username
        string password
        string email
        datetime created_at
        datetime updated_at
    }
    
    ROLE {
        int id PK
        string name
    }
    
    USERS_ROLES {
        int user_id FK
        int role_id FK
    }
    
    MONITORINGS {
        bigint id PK
        text fact
        text mind
        text why_correct
        text why_doubt
        text new_thought
        int user_id FK
        datetime created_at
        datetime updated_at
    }
    
    DISTORTION_LISTS {
        bigint id PK
        string distortion_name
        string info
        datetime created_at
        datetime updated_at
    }
    
    MONITORING_DISTORTION_RELATIONS {
        bigint id PK
        bigint monitoring_id FK
        bigint distortion_list_id FK
        datetime created_at
        datetime updated_at
    }
    
    NEGATIVE_FEELS {
        bigint id PK
        string negative_feel_name
        datetime created_at
        datetime updated_at
    }
    
    POSITIVE_FEELS {
        bigint id PK
        string positive_feel_name
        datetime created_at
        datetime updated_at
    }
    
    MONITORING_NEGATIVE_FEELS {
        bigint id PK
        bigint monitoring_id FK
        bigint negative_feel_id FK
        datetime created_at
        datetime updated_at
    }
    
    MONITORING_POSITIVE_FEELS {
        bigint id PK
        bigint monitoring_id FK
        bigint positive_feel_id FK
        datetime created_at
        datetime updated_at
    }
    
    CBT_BASICS {
        int id PK
        text fact
        text mind
        text body
        text behavior
        int user_id FK
        datetime created_at
        datetime updated_at
    }
    
    
    USER ||--o{ MONITORINGS : "has"
    USER ||--o{ CBT_BASICS : "has"
    USER }|--o{ USERS_ROLES : "has"
    USERS_ROLES }o--|| ROLE : "belongs to"
    
    MONITORINGS ||--o{ MONITORING_DISTORTION_RELATIONS : "has"
    MONITORING_DISTORTION_RELATIONS }o--|| DISTORTION_LISTS : "references"
    
    MONITORINGS ||--o{ MONITORING_NEGATIVE_FEELS : "has"
    MONITORING_NEGATIVE_FEELS }o--|| NEGATIVE_FEELS : "references"
    
    MONITORINGS ||--o{ MONITORING_POSITIVE_FEELS : "has"
    MONITORING_POSITIVE_FEELS }o--|| POSITIVE_FEELS : "references"
```

## テーブル説明

### ユーザー認証関連
- **USER**: アプリケーションのユーザー情報を格納
- **ROLE**: ユーザーロール（EMPLOYEE, MANAGER, ADMIN）を格納
- **USERS_ROLES**: ユーザーとロールの多対多関係を管理する中間テーブル

### 思考モニタリング関連
- **MONITORINGS**: ユーザーの思考モニタリング記録を格納
- **DISTORTION_LISTS**: 認知の歪みの種類とその説明を格納
- **MONITORING_DISTORTION_RELATIONS**: モニタリングと認知の歪みの関連を管理する中間テーブル
- **NEGATIVE_FEELS**: ネガティブな感情の種類を格納
- **POSITIVE_FEELS**: ポジティブな感情の種類を格納
- **MONITORING_NEGATIVE_FEELS**: モニタリングとネガティブな感情の関連を管理する中間テーブル
- **MONITORING_POSITIVE_FEELS**: モニタリングとポジティブな感情の関連を管理する中間テーブル

### CBT（認知行動療法）関連
- **CBT_BASICS**: CBTの基本情報を格納

## 関連性
- ユーザーは複数のモニタリング、CBT基本情報を持つことができる
- ユーザーは複数のロールを持つことができる
- モニタリングは複数の認知の歪み、ネガティブな感情、ポジティブな感情と関連付けることができる