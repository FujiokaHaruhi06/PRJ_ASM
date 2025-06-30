USE [ASM]
GO

INSERT INTO [dbo].[Division]
           ([divname]
           ,[headid])
     VALUES
           ('IT', null),
		   ('QA', null),
		   ('Sale', null)
GO

INSERT INTO [dbo].[Group]
           ([mgrid]
           ,[divid])
     VALUES
           (null,null),
		   (null,null),
		   (null,null)
GO

INSERT INTO [dbo].[Feature] ([fname], [link], [description]) VALUES
    (N'Trang chủ', '/home', N'Hiển thị trang chủ'),
    (N'Tạo đơn', '/create_leave_application', N'Tạo đơn xin nghỉ'),
    (N'Tình trạng', '/application_status', N'Xem tình trạng đơn'),
    (N'Xét duyệt', '/application_review', N'Xét duyệt đơn xin nghỉ'),
    (N'Lịch làm việc', '/work_schedule', N'Xem lịch làm việc');
GO

INSERT INTO [dbo].[Role]
           ([rname])
     VALUES
           ('Division Leader'),
		   ('Group Leader'),
		   ('Member')
GO

-- Thêm dữ liệu cho User (chỉ thông tin cá nhân, có phone, không có gid)
INSERT INTO [dbo].[User] ([email], [phone], [firstname], [lastname]) VALUES
    ('divlead@company.com', '0900000001', N'Nguyen', N'Van A'),
    ('grouplead@company.com', '0900000002', N'Tran', N'Thi B'),
    ('member11@company.com', '0900000003', N'Le', N'Van C'),
    ('member12@company.com', '0900000004', N'Pham', N'Thi D'),
    ('grouplead2@company.com', '0900000005', N'Tran', N'Thi E'),
    ('member21@company.com', '0900000006', N'Le', N'Van F'),
    ('member22@company.com', '0900000007', N'Pham', N'Thi D'),
    ('grouplead3@company.com', '0900000008', N'Tran', N'Thi G'),
    ('member31@company.com', '0900000009', N'Le', N'Van H'),
    ('member32@company.com', '0900000010', N'Pham', N'Thi I')
GO

-- Thêm dữ liệu cho Account (mỗi user có thể có nhiều account, active=1 là tài khoản hiện tại, có gid)
INSERT INTO [dbo].[Account] ([uid], [gid], [username], [password], [active]) VALUES
    (1, 1, N'divlead1', 'hashedpassword1', 1),
    (2, 1, N'grouplead1', 'hashedpassword2', 1),
    (3, 1, N'member11', 'hashedpassword3', 1),
    (4, 1, N'member12', 'hashedpassword4', 1),
    (5, 2, N'grouplead2', 'hashedpassword2', 1),
    (6, 2, N'member21', 'hashedpassword3', 1),
    (7, 2, N'member22', 'hashedpassword4', 1),
    (8, 3, N'grouplead3', 'hashedpassword2', 1),
    (9, 3, N'member31', 'hashedpassword3', 1),
    (10, 3, N'member32', 'hashedpassword4', 1)
GO

-- Cập nhật Group và Division
UPDATE [dbo].[Group] SET divid = 1, mgrid = 2 WHERE gid = 1;
UPDATE [dbo].[Group] SET divid = 2, mgrid = 5 WHERE gid = 2;
UPDATE [dbo].[Group] SET divid = 3, mgrid = 8 WHERE gid = 3;
GO
UPDATE [dbo].[Division] SET headid = 1 WHERE divid = 1;
GO

-- 7. Thêm dữ liệu cho LA_status
INSERT INTO [dbo].[LA_status] ([sname]) VALUES
    (N'Pending'),
    (N'Approved'),
    (N'Rejected');
GO

-- 8. Thêm dữ liệu cho Role_Feature
INSERT INTO [dbo].[Role_Feature] (rid, fid) VALUES
(3, 1), (3, 2), (3, 3),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);
GO

-- Thêm dữ liệu cho User_Role (liên kết aid, rid)
INSERT INTO [dbo].[User_Role] (aid, rid) VALUES
(1, 1),
(2, 2), (5, 2), (8, 2),
(3, 3), (4, 3), (6, 3), (7, 3), (9, 3), (10, 3);
GO

-- 12. Thêm dữ liệu mẫu cho Leave_Application (liên kết với aid)
INSERT INTO [dbo].[Leave_Application] (aid, start_date, end_date, create_time, reason, sid, approver_aid, approval_time) VALUES
(3, '2024-06-01', '2024-06-02', GETDATE(), N'Nghỉ phép cá nhân', 1, 2, NULL),
(4, '2024-06-03', '2024-06-04', GETDATE(), N'Nghỉ ốm', 1, 2, NULL);
GO