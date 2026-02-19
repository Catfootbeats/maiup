# MaiUp!!!

一个使用 `Kotlin Multiplatform` 构建的 `舞萌 | 中二` 传分器，支持 `落雪 | 水鱼` 查分器。

## 功能 RoadMap

- [x] 更换主题
- [ ] 上传成绩
- [ ] 查询歌曲
- [ ] 查询 B50
- [ ] 支持 CHUNITHM
- [ ] 支持落雪 OAuth
- [ ] 其他功能 ...
- [ ] 适配 ios (有了Mac再说)

## Bug 修复

- [ ] jvm 平台下无法实时随系统变换主题颜色

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
