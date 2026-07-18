-- =============================================
-- 完整初始化脚本
-- 建表 → 清表 → 插入数据（UTF-8 编码）
-- 可重复执行，不会报错
-- =============================================

-- ========== 0. 设置 UTF-8 字符集 ==========
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- ========== 1. 建表（不存在才创建） ==========

CREATE TABLE IF NOT EXISTS dept
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS emp_expr
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    emp_id     INT NOT NULL,
    begin_date DATE,
    end_date   DATE,
    company    VARCHAR(100),
    job        VARCHAR(100),
    FOREIGN KEY (emp_id) REFERENCES emp(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========== 2. 清空旧数据 ==========

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE emp_expr;
TRUNCATE TABLE account;
TRUNCATE TABLE emp;
TRUNCATE TABLE dept;

SET FOREIGN_KEY_CHECKS = 1;

-- ========== 3. 插入部门数据 ==========

INSERT INTO dept (id, name, create_time, update_time) VALUES
(1,  '技术部',   '2025-01-01 09:00:00', '2026-06-15 10:00:00'),
(2,  '产品部',   '2025-01-01 09:00:00', '2026-06-15 10:00:00'),
(3,  '市场部',   '2025-02-01 09:00:00', '2026-06-15 10:00:00'),
(4,  '人事部',   '2025-02-01 09:00:00', '2026-06-15 10:00:00'),
(5,  '财务部',   '2025-03-01 09:00:00', '2026-06-15 10:00:00'),
(6,  '设计部',   '2025-03-15 09:00:00', '2026-06-15 10:00:00'),
(7,  '运维部',   '2025-04-01 09:00:00', '2026-06-15 10:00:00'),
(8,  '客服部',   '2025-04-01 09:00:00', '2026-06-15 10:00:00'),
(9,  '法务部',   '2025-05-01 09:00:00', '2026-06-15 10:00:00'),
(10, '行政部',   '2025-06-01 09:00:00', '2026-06-15 10:00:00');

-- ========== 4. 插入员工数据 ==========

INSERT INTO emp (id, username, password, name, gender, phone, job, salary, image, entry_date, create_time, update_time, dept_id) VALUES
-- 技术部 (dept_id=1)
(1,  'zhangs',     '123456', '张三',   1, '13800138001', 1, 25000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangs',     '2022-03-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 1),
(2,  'lisi',       '123456', '李四',   1, '13800138002', 2, 18000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=lisi',       '2023-06-15', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 1),
(3,  'wangw',      '123456', '王五',   1, '13800138003', 2, 16000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=wangw',      '2024-01-10', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 1),
(4,  'zhaol',      '123456', '赵六',   0, '13800138004', 3, 12000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhaol',      '2024-09-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 1),
-- 产品部 (dept_id=2)
(5,  'sunq',       '123456', '孙七',   0, '13800138005', 1, 22000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=sunq',       '2022-07-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 2),
(6,  'zhoub',      '123456', '周八',   1, '13800138006', 2, 15000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhoub',      '2023-11-20', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 2),
-- 市场部 (dept_id=3)
(7,  'wuj',        '123456', '吴九',   0, '13800138007', 1, 20000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=wuj',        '2023-04-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 3),
(8,  'zhengw',     '123456', '郑十',   1, '13800138008', 2, 14000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhengw',     '2024-02-15', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 3),
-- 人事部 (dept_id=4)
(9,  'chenm',      '123456', '陈明',   1, '13800138009', 1, 18000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=chenm',      '2022-09-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 4),
(10, 'liuj',       '123456', '刘娟',   0, '13800138010', 2, 11000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=liuj',       '2024-05-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 4),
-- 财务部 (dept_id=5)
(11, 'huangw',     '123456', '黄伟',   1, '13800138011', 1, 23000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=huangw',     '2021-12-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 5),
(12, 'yangx',      '123456', '杨雪',   0, '13800138012', 2, 13000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=yangx',      '2024-08-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 5),
-- 设计部 (dept_id=6)
(13, 'linh',       '123456', '林浩',   1, '13800138013', 1, 19000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=linh',       '2023-04-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 6),
(14, 'hexin',      '123456', '何欣',   0, '13800138014', 2, 14000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=hexin',      '2024-06-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 6),
-- 运维部 (dept_id=7)
(15, 'maq',        '123456', '马强',   1, '13800138015', 1, 21000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=maq',        '2022-06-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 7),
(16, 'gaof',       '123456', '高飞',   1, '13800138016', 2, 13000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=gaof',       '2024-11-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 7),
-- 客服部 (dept_id=8)
(17, 'caol',       '123456', '曹丽',   0, '13800138017', 1, 15000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=caol',       '2023-08-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 8),
(18, 'xuw',        '123456', '徐文',   0, '13800138018', 2, 9000,  'https://api.dicebear.com/7.x/avataaars/svg?seed=xuw',        '2025-03-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 8),
-- 法务部 (dept_id=9)
(19, 'dengz',      '123456', '邓志',   1, '13800138019', 1, 24000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=dengz',      '2021-09-01', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 9),
-- 行政部 (dept_id=10)
(20, 'lif',        '123456', '李芳',   0, '13800138020', 1, 16000, 'https://api.dicebear.com/7.x/avataaars/svg?seed=lif',        '2023-01-15', '2025-01-01 09:00:00', '2026-06-20 10:00:00', 10);

-- ========== 5. 插入工作经历 ==========

INSERT INTO emp_expr (id, emp_id, begin_date, end_date, company, job) VALUES
-- 张三
(1,  1,  '2018-03-01', '2020-05-31', '阿里巴巴',       'Java 开发工程师'),
(2,  1,  '2020-06-01', '2022-02-28', '腾讯科技',        '高级 Java 开发工程师'),
-- 李四
(3,  2,  '2019-07-01', '2021-12-31', '网易',            '前端开发工程师'),
(4,  2,  '2022-01-01', '2023-05-31', '字节跳动',        '全栈开发工程师'),
-- 王五
(5,  3,  '2020-09-01', '2023-12-31', '美团',            '测试工程师'),
-- 孙七
(6,  5,  '2017-04-01', '2020-08-31', '百度',            '产品经理'),
(7,  5,  '2020-09-01', '2022-06-30', '京东',            '高级产品经理'),
-- 吴九
(8,  7,  '2019-01-01', '2021-12-31', '华为',            '市场专员'),
(9,  7,  '2022-01-01', '2023-03-31', '小米',            '市场总监'),
-- 陈明
(10, 9,  '2018-06-01', '2021-05-31', '中兴',            'HR 专员'),
-- 黄伟
(11, 11, '2016-03-01', '2019-02-28', '德勤',            '财务分析师'),
(12, 11, '2019-03-01', '2021-11-30', '普华永道',        '高级财务经理'),
-- 林浩
(13, 13, '2019-08-01', '2021-07-31', '网易游戏',        'UI 设计师'),
(14, 13, '2021-08-01', '2023-03-31', '米哈游',          '资深 UI 设计师'),
-- 马强
(15, 15, '2018-01-01', '2020-12-31', '中国移动',        '运维工程师'),
(16, 15, '2021-01-01', '2022-05-31', '阿里云',          '高级运维工程师'),
-- 邓志
(17, 19, '2017-07-01', '2019-06-30', '金杜律师事务所',   '法务助理'),
(18, 19, '2019-07-01', '2021-08-31', '中伦律师事务所',   '律师'),
-- 李芳
(19, 20, '2019-04-01', '2022-03-31', '万科',            '行政专员');

-- ========== 6. 插入账号 ==========

INSERT INTO account (id, username, password, emp_id, role, status, create_time, update_time) VALUES
(1,  'zhangs',   '123456', 1,  'ADMIN', 1, '2025-01-01 09:00:00', '2026-07-01 10:00:00'),
(2,  'lisi',     '123456', 2,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(3,  'wangw',    '123456', 3,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(4,  'zhaol',    '123456', 4,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(5,  'sunq',     '123456', 5,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(6,  'zhoub',    '123456', 6,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(7,  'wuj',      '123456', 7,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(8,  'zhengw',   '123456', 8,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(9,  'chenm',    '123456', 9,  'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(10, 'liuj',     '123456', 10, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(11, 'huangw',   '123456', 11, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(12, 'yangx',    '123456', 12, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(13, 'linh',     '123456', 13, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(14, 'hexin',    '123456', 14, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(15, 'maq',      '123456', 15, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(16, 'gaof',     '123456', 16, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(17, 'caol',     '123456', 17, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(18, 'xuw',      '123456', 18, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(19, 'dengz',    '123456', 19, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
(20, 'lif',      '123456', 20, 'USER',  1, '2025-01-01 09:00:00', '2026-06-20 10:00:00'),
-- 独立管理员
(21, 'admin',    '123456', NULL, 'ADMIN', 1, '2025-01-01 09:00:00', '2026-07-01 10:00:00');

-- ========== 7. 验证数据 ==========

SELECT '--- DEPT ---' AS '';
SELECT * FROM dept;

SELECT '--- EMP ---' AS '';
SELECT id, name, username, job, salary, dept_id FROM emp;
