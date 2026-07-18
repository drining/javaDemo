-- ========================================
-- 数据库表结构
-- ========================================

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
