# mirai-console-automations

## 功能：

### 配置发送消息的对象实现自动向对方发送消息续火

```json
{
  "local": true,
  "setting": {
    "account1": {
      "target": "message"
    },
    "account2": {
      "target": "message"
    }
  }
}
```

* `local: true` 本地运行，登录即发送完成后退出
* `local: false` 服务器运行，每天12点自动发送
* 不提供自设时间，防止高频发送无用消息

### 功能请求

* 该项目的目的是实现一些自动化的功能，目前我自己的需求是续火，欢迎提交Issue给我一些点子
