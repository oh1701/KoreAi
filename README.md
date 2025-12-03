# KoreAi

**KoreAi**는 **Jetpack Compose**와 **Clean Architecture**를 기반으로 구축된 안드로이드 AI 이미지 생성 애플리케이션입니다.

기존 다른 이미지 생성 앱과는 달리 **Google Translation API** 와 연동하여 한국어로 이미지 생성이 가능합니다.

## 🛠 기술 스택 (Tech Stack)

*   **Language**: Kotlin
*   **UI**: Jetpack Compose
*   **Architecture**: Clean Architecture + MVVM
*   **Asynchronous**: Coroutines & Flow
*   **Network**: Ktor
*   **Local DB**: Room
*   **DI**: Hilt
*   **Image Loading**: Coil
*   **Navigation**: Navigation Compose (Custom KSP Processor 활용)
*   **Others**: Firebase, AdMob, Lottie

## 🏗 아키텍처 (Architecture)

이 프로젝트는 확장성, 테스트 용이성, 유지보수성을 보장하기 위해 **Clean Architecture** 원칙을 따릅니다.

### 모듈 구조
*   **app**: 애플리케이션 진입점 및 DI 설정
*   **presentation**: UI (Compose) 및 ViewModel
*   **domain**: 비즈니스 로직 (UseCase) 및 레포지토리 인터페이스 (순수 Kotlin)
*   **data**: 데이터 구현 (Repository, Ktor, Room)
*   **core**: 공통 유틸리티 및 확장 함수
*   **processor**: 내비게이션 그래프 자동 등록을 위한 커스텀 KSP 프로세서

### 아키텍처 다이어그램

```mermaid
graph TD
    subgraph "Presentation Layer (UI)"
        UI["Compose UI<br/>(Activity/Screen)"]
        VM[ViewModel]
        UI -->|Observe State| VM
        VM -->|User Action| UI
    end

    subgraph "Domain Layer (Business Logic)"
        UC[UseCase]
        RepoInt[Repository Interface]
        Model[Domain Model]
        
        VM -->|Uses| UC
        UC -->|Uses| RepoInt
        RepoInt -.->|Returns| Model
    end

    subgraph "Data Layer (Data Access)"
        RepoImpl[Repository Implementation]
        Remote["Remote Data Source<br/>(Ktor)"]
        Local["Local Data Source<br/>(Room)"]
        
        RepoImpl -->|Implements| RepoInt
        RepoImpl -->|Calls| Remote
        RepoImpl -->|Calls| Local
    end

    subgraph "Processor (Code Gen)"
        KSP[NavGraphRegisterProcessor]
        KSP -.->|Generates| UI
    end
    
    %% Styling
    classDef layer fill:#f9f9f9,stroke:#333,stroke-width:2px;
    class UI,VM,UC,RepoInt,RepoImpl,Remote,Local,Model layer
```

## 🔄 데이터 흐름 (Data Flow)

다음 시퀀스 다이어그램은 사용자가 이미지 생성을 요청했을 때의 데이터 흐름을 보여줍니다.

```mermaid
sequenceDiagram
    participant User
    participant Screen as HomeScreen (Compose)
    participant VM as HomeViewModel
    participant UC as FetchFluxImageUseCase
    participant Repo as FluxImageRepository
    participant API as Ktor Client
    
    User->>Screen: Click "Generate Image"
    Screen->>VM: createFluxImage()
    VM->>UC: invoke(prompt)
    UC->>Repo: fetchFluxImage(prompt)
    Repo->>API: POST /generate
    API-->>Repo: JSON Response
    Repo-->>UC: FluxImageRes (Domain Entity)
    UC-->>VM: Result<FluxImageRes>
    VM->>VM: update StateFlow
    VM-->>Screen: Collect State (Success)
    Screen-->>User: Show Generated Image
```

## ✨ 주요 기능 (Key Features)

*   **텍스트-이미지 생성**: 상세한 텍스트 프롬프트를 사용하여 이미지 생성
*   **프롬포트 관리**: 이전에 생성된 이미지 프롬포트를 로컬에서 확인 및 관리
*   **커스텀 내비게이션**: KSP를 활용한 내비게이션 그래프 자동 등록