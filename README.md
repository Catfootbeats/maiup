# MaiUp!!!

一个用 `Kotlin Multiplatform` 构建的 `舞萌 | 中二` 传分器，支持 `落雪 | 水鱼` 查分器。

## 功能

- [x] 更换主题 
- [ ] 上传成绩
- [ ] 查询歌曲
- [ ] 查询 B50
- [ ] 适配 ios
- [ ] 其他功能 ...

## 构建

### Android

通过 IDE 构建或通过终端构建

- macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Desktop (JVM)

通过 IDE 构建或通过终端构建

- macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### iOS

> 因为我没有 Mac，所以 actual 代码都是 AI 实现的，也没有配置 koin

通过 IDE 构建或者直接用 XCode 打开 [iosApp](./iosApp) 构建
