CREATE DATABASE ASM

USE ASM
GO

-- Bảng User (chỉ lưu thông tin cá nhân, không liên kết group)
CREATE TABLE [User] (
    uid INT PRIMARY KEY IDENTITY,
    email NVARCHAR(255),
    firstname NVARCHAR(100),
    lastname NVARCHAR(100)
);

-- Bảng Group
CREATE TABLE [Group] (
    gid INT PRIMARY KEY IDENTITY(1,1),
    mgrid INT NULL,  -- Cho phép NULL nếu group chưa có manager (aid của Account)
    divid INT NULL   -- Cho phép NULL nếu group chưa thuộc division nào
);

-- Bảng Division
CREATE TABLE Division (
    divid INT PRIMARY KEY IDENTITY(1,1),
    divname NVARCHAR(50) NOT NULL,
    headid INT NULL -- Cho phép NULL nếu division chưa có trưởng phòng (aid của Account)
);

-- Bảng Role
CREATE TABLE [Role] (
    rid INT PRIMARY KEY IDENTITY(1,1),
    rname VARCHAR(50) NOT NULL
);

-- Bảng Feature
CREATE TABLE Feature (
    fid INT PRIMARY KEY IDENTITY(1,1),
    fname NVARCHAR(50) NOT NULL,
    link VARCHAR(50) NOT NULL,
    description NVARCHAR(255) NULL
);

-- Bảng Account (tài khoản đăng nhập, liên kết với User, Group)
CREATE TABLE Account (
    aid INT PRIMARY KEY IDENTITY,
    uid INT NOT NULL,
    username NVARCHAR(100) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    gid INT, -- nếu cần liên kết group
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    mgrid INT NULL,
    FOREIGN KEY (uid) REFERENCES [User](uid)
);

-- Bảng User_Role (liên kết account với role)
CREATE TABLE Account_Role (
    aid INT NOT NULL,
    rid INT NOT NULL,
    PRIMARY KEY (aid, rid),
    FOREIGN KEY (aid) REFERENCES Account(aid),
    FOREIGN KEY (rid) REFERENCES [Role](rid)
);

-- Bảng Role_Feature
CREATE TABLE Role_Feature (
    rid INT NOT NULL,
    fid INT NOT NULL,
    PRIMARY KEY (rid, fid),
    FOREIGN KEY (rid) REFERENCES [Role](rid),
    FOREIGN KEY (fid) REFERENCES Feature(fid)
);

-- Bảng LA_status
CREATE TABLE LA_status (
    sid INT PRIMARY KEY IDENTITY(1,1),
    sname NVARCHAR(50) NOT NULL
);

-- Bảng Leave_Application (liên kết với Account)
CREATE TABLE Leave_Application (
    lid INT PRIMARY KEY IDENTITY(1,1),
    aid INT NOT NULL, -- Người tạo đơn (Account)
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    create_time DATETIME NOT NULL,
    reason NVARCHAR(MAX) NOT NULL,
    sid INT NOT NULL,
    approver_aid INT NULL,      -- Người duyệt (Account)
    approval_time DATETIME NULL,
    FOREIGN KEY (aid) REFERENCES Account(aid),
    FOREIGN KEY (sid) REFERENCES LA_status(sid),
    FOREIGN KEY (approver_aid) REFERENCES Account(aid)
);

-- Khóa ngoại cho Group
ALTER TABLE [Group]
ADD CONSTRAINT FK_Group_Division FOREIGN KEY (divid) REFERENCES Division(divid);
ALTER TABLE [Group]
ADD CONSTRAINT FK_Group_Account FOREIGN KEY (mgrid) REFERENCES Account(aid);

ALTER TABLE [Division]
ADD CONSTRAINT FK_Division_Account FOREIGN KEY (headid) REFERENCES Account(aid);

-- Trigger cập nhật approval_time
DROP TRIGGER IF EXISTS TRG_LeaveApplication_Update;
CREATE TRIGGER TRG_LeaveApplication_Update
ON Leave_Application
AFTER UPDATE
AS
BEGIN
    IF (SELECT COUNT(*) FROM inserted) > 0
    BEGIN
        UPDATE Leave_Application
            SET approval_time = GETDATE()
        FROM Leave_Application la
        INNER JOIN inserted i ON la.lid = i.lid;
    END
END;
GO