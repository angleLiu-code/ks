-- 创建数据库
CREATE DATABASE IF NOT EXISTS repair_system DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE repair_system;

-- 用户表 (users)
CREATE TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
  password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
  real_name VARCHAR(100) COMMENT '真实姓名',
  email VARCHAR(100) COMMENT '邮箱',
  phone VARCHAR(20) COMMENT '电话',
  dorm_id VARCHAR(50) COMMENT '宿舍号',
  role ENUM('student', 'maintenance', 'admin') NOT NULL DEFAULT 'student' COMMENT '用户角色',
  status INT DEFAULT 1 COMMENT '状态: 1激活 0禁用',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_username (username),
  INDEX idx_role (role),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 报修分类表 (repair_categories)
CREATE TABLE IF NOT EXISTS repair_categories (
  category_id INT PRIMARY KEY AUTO_INCREMENT,
  category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
  description TEXT COMMENT '分类描述',
  is_active INT DEFAULT 1 COMMENT '是否激活',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修分类表';

-- 报修申请表 (repair_requests)
CREATE TABLE IF NOT EXISTS repair_requests (
  request_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL COMMENT '学生ID',
  category_id INT NOT NULL COMMENT '分类ID',
  title VARCHAR(200) NOT NULL COMMENT '报修标题',
  description TEXT COMMENT '故障描述',
  dorm_number VARCHAR(50) COMMENT '宿舍号',
  image_url VARCHAR(255) COMMENT '故障图片URL',
  priority ENUM('low', 'medium', 'high') DEFAULT 'medium' COMMENT '优先级',
  status ENUM('submitted', 'assigned', 'processing', 'completed', 'rejected') DEFAULT 'submitted' COMMENT '状态',
  rejection_reason TEXT COMMENT '拒绝原因',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (student_id) REFERENCES users(user_id),
  FOREIGN KEY (category_id) REFERENCES repair_categories(category_id),
  INDEX idx_student_id (student_id),
  INDEX idx_status (status),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修申请表';

-- 维修任务表 (repair_tasks)
CREATE TABLE IF NOT EXISTS repair_tasks (
  task_id INT PRIMARY KEY AUTO_INCREMENT,
  request_id INT NOT NULL COMMENT '报修申请ID',
  maintenance_id INT COMMENT '维修人员ID',
  assigned_at TIMESTAMP NULL COMMENT '分配时间',
  started_at TIMESTAMP NULL COMMENT '开始时间',
  completed_at TIMESTAMP NULL COMMENT '完成时间',
  maintenance_notes TEXT COMMENT '维修备注',
  repair_result TEXT COMMENT '维修结果',
  parts_used VARCHAR(200) COMMENT '使用配件',
  labor_hours DECIMAL(5,2) COMMENT '劳动时数',
  status ENUM('pending', 'accepted', 'in_progress', 'completed') DEFAULT 'pending' COMMENT '状态',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (request_id) REFERENCES repair_requests(request_id),
  FOREIGN KEY (maintenance_id) REFERENCES users(user_id),
  INDEX idx_request_id (request_id),
  INDEX idx_maintenance_id (maintenance_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修任务表';

-- 维修评价表 (repair_evaluations)
CREATE TABLE IF NOT EXISTS repair_evaluations (
  evaluation_id INT PRIMARY KEY AUTO_INCREMENT,
  request_id INT NOT NULL COMMENT '报修申请ID',
  student_id INT NOT NULL COMMENT '学生ID',
  rating INT CHECK(rating BETWEEN 1 AND 5) COMMENT '评分: 1-5星',
  comment TEXT COMMENT '评价内容',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (request_id) REFERENCES repair_requests(request_id),
  FOREIGN KEY (student_id) REFERENCES users(user_id),
  INDEX idx_request_id (request_id),
  INDEX idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='维修评价表';

-- 管理员日志表 (admin_logs)
CREATE TABLE IF NOT EXISTS admin_logs (
  log_id INT PRIMARY KEY AUTO_INCREMENT,
  admin_id INT NOT NULL COMMENT '管理员ID',
  action VARCHAR(100) COMMENT '操作类型',
  target_type VARCHAR(50) COMMENT '目标类型: user/request/task',
  target_id INT COMMENT '目标ID',
  details TEXT COMMENT '操作详情',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (admin_id) REFERENCES users(user_id),
  INDEX idx_admin_id (admin_id),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员日志表';

-- 插入初始数据
-- 报修分类
INSERT INTO repair_categories (category_name, description) VALUES
('灯具损坏', '宿舍内灯具故障或损坏'),
('水管漏水', '宿舍内水管破损或漏水'),
('空调故障', '宿舍空调不制冷或制热'),
('门窗故障', '宿舍门窗损坏或无法正常开关'),
('床铺损坏', '宿舍床铺损坏或无法使用'),
('电源故障', '宿舍电源或插座故障'),
('其他问题', '其他维修问题');

-- 插入初始用户
-- 学生用户 (密码都是123456, MD5加密)
INSERT INTO users (username, password, real_name, email, phone, dorm_id, role, status) VALUES
('student01', '202cb962ac59075b964b07152d234b70', '张三', 'student01@university.edu', '13800138001', '5-201', 'student', 1),
('student02', '202cb962ac59075b964b07152d234b70', '李四', 'student02@university.edu', '13800138002', '5-202', 'student', 1),
('student03', '202cb962ac59075b964b07152d234b70', '王五', 'student03@university.edu', '13800138003', '5-203', 'student', 1);

-- 维修人员 (密码都是123456)
INSERT INTO users (username, password, real_name, email, phone, role, status) VALUES
('maintenance01', '202cb962ac59075b964b07152d234b70', '李师傅', 'maintenance01@university.edu', '13900139001', 'maintenance', 1),
('maintenance02', '202cb962ac59075b964b07152d234b70', '王师傅', 'maintenance02@university.edu', '13900139002', 'maintenance', 1);

-- 管理员 (密码都是123456)
INSERT INTO users (username, password, real_name, email, phone, role, status) VALUES
('admin', '202cb962ac59075b964b07152d234b70', '系统管理员', 'admin@university.edu', '13700137000', 'admin', 1);

COMMIT;