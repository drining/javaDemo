-- =============================================
-- 完整数据库初始化脚本
-- 包含：建库 → 建表 → 插入测试数据
-- =============================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS demo3
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE demo3;

-- 3. 建表
CREATE TABLE IF NOT EXISTS dept
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS emp
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    gender      TINYINT      NOT NULL COMMENT '1=男, 0=女',
    phone       VARCHAR(20),
    job         TINYINT      COMMENT '1=主任, 2=主管, 3=员工',
    salary      DECIMAL(10, 2),
    image       VARCHAR(500),
    entry_date  DATE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    dept_id     INT,
    FOREIGN KEY (dept_id) REFERENCES dept(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS emp_expr
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    emp_id     INT NOT NULL,
    begin_date DATE,
    end_date   DATE,
    company    VARCHAR(100),
    job        VARCHAR(100),
    FOREIGN KEY (emp_id) REFERENCES emp(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS account
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    emp_id      INT          NULL COMMENT '关联员工ID',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN, USER',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1=启用, 0=禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. 插入部门数据
INSERT INTO dept (name) VALUES
('技术部'),
('人事部'),
('财务部'),
('市场部');

-- 5. 插入员工数据
INSERT INTO emp (username, password, name, gender, phone, job, salary, entry_date, dept_id) VALUES
('zhangsan', '123456', '张三', 1, '13800138001', 1, 15000.00, '2023-01-15', 1),
('lisi',     '123456', '李四', 1, '13800138002', 2, 12000.00, '2023-03-20', 1),
('xiaohong', '123456', '小红', 0, '13800138003', 3, 8000.00,  '2024-06-01', 1),
('zhaoliu',  '123456', '赵六', 1, '13800138004', 2, 11000.00, '2024-09-10', 2),
('sunqi',    '123456', '孙七', 1, '13800138005', 2, 10500.00, '2024-11-01', 3),
('zhouba',   '123456', '周八', 0, '13800138006', 3, 7500.00,  '2025-02-15', 4);

-- 6. 插入员工工作经历
INSERT INTO emp_expr (emp_id, begin_date, end_date, company, job) VALUES
(1, '2020-03-01', '2022-12-31', '阿里云', 'Java开发工程师'),
(1, '2023-01-15', NULL,         '本公司', '技术主任'),
(2, '2019-07-01', '2023-02-28', '腾讯',   '高级开发'),
(2, '2023-03-20', NULL,         '本公司', '技术主管'),
(4, '2022-06-01', '2024-08-31', '华为',   '财务专员'),
(4, '2024-09-10', NULL,         '本公司', '财务主管');

-- 7. 插入账号
INSERT INTO account (username, password, emp_id, role) VALUES
('admin',    'admin123', 1, 'ADMIN'),
('zhangsan', '123456',   1, 'USER'),
('lisi',     '123456',   2, 'USER'),
('xiaohong', '123456',   3, 'USER'),
('zhaoliu',  '123456',   4, 'USER');

-- 8. 验证
SELECT '========== 部门 ==========' AS '';
SELECT * FROM dept;

SELECT '========== 员工 ==========' AS '';
SELECT * FROM emp;

SELECT '========== 工作经历 ==========' AS '';
SELECT * FROM emp_expr;

SELECT '========== 账号 ==========' AS '';
SELECT * FROM account;

SELECT '========== 员工-部门 关联查询 ==========' AS '';
SELECT e.id, e.name AS 员工姓名, e.job, e.salary, d.name AS 部门
FROM emp e
LEFT JOIN dept d ON e.dept_id = d.id;
