# 在Oracle SQLcl命令行工具上落地 代码覆盖率
- Oracle SQL开发者命令行工具[Oracle SQLcl](https://www.oracle.com/database/technologies/appdev/sqlcl.html) ，通过命令行访问Oracle数据库。
- SQLcl能够以交互方式，或者批方式执行 SQL 和 PL/SQL. SQLcl 提供 内联编辑，语句完成，以及命令重调用等丰富体验功能,完全支持基于SQL*Plus命令工具的脚本，以及其环境配置，命令和行为.

## 优势
- Oracle SQL Developer 是Oracle开发者必装工具(即使日常更习惯使用PLSQL Developer)，自身集成了SQLcl工具(`sqldeveloper\bin\sql`)，GUI中有直接调用 SQLcl 按钮；也就是说Oracle开发者都有SQLcl工具，具备普适性。
- 批执行有大量通过shell脚本调用SQL*Plus的方式；不仅支持本地，也利于持续集成环境；
- SQLcl使用java开发，非开源。可直接引用jar包调用，以及通过反编译掌握调用接口；

## 可能的落地方式
### 客户端脚本：
- SQLcl 支持客户端jvm脚本，比如javascript，可以用来操作查询结果,构建动态命令，交互会话等；
- 优点：
  - 在工具上非侵入式扩展，不改变使用习惯；

### 插件：
- SQLcl 自身应有插件机制，如[PGQL](https://docs.oracle.com/en/database/oracle/sql-developer-command-line/21.2/sqcug/using-pgql-plug-sqlcl.html#GUID-E0EFA43F-003F-4C8C-8056-54E9A428B8B7), 在其`lib/ext`目录下安装，但缺乏相应文档；
- 优点：在工具上非侵入式扩展，不改变使用习惯；

### Shell 脚本；
- SQLcl 在linux平台自身是通过SH脚本为入口调用jar包执行，可通过扩展SH脚本实现覆盖率；
- 优点：
  - 在工具上非侵入式扩展；
- 缺点：
  - 改变入口文件，稍改变使用习惯；

### java封装：
- 用java封装SQLcl，作为替代入口
- 优点：
  - 在工具上非侵入式扩展；但可能产生版本依赖；
- 缺点：
    - 改变入口，稍改变使用习惯；

