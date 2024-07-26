# 架构

ip -> apisix -> host:port -> 第一层uri -> 基础部门前端

ip -> apisix -> host:port -> 第一层uri -> 所有部门微前端

ip -> apisix -> host:port -> 第一层uri -> 基础部门 -> gateway -> 第二层uri -> 服务

ip -> apisix -> host:port -> 第一层uri -> 其他部门 -> gateway -> 第二层uri -> 服务

顶层命名空间：kubesphere

子层命名空间：apisix、前端、部门

# 前端

## 基础部门前端
- 菜单管理
- 权限管理
- 角色管理
- 字典管理

## 所有部门微前端
- 基础部门
- 套接字部门
- 公告部门
- 支付部门
- 文件部门
- 通讯部门
- 运维部门
-- 监控部门
 