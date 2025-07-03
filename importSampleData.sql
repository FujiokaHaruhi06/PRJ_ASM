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

INSERT INTO [dbo].[LA_status] ([sname]) VALUES
    (N'Pending'),
    (N'Approved'),
    (N'Rejected');
GO

-- Phân quyền cho các vai trò
-- Member chỉ có 3 quyền đầu
INSERT INTO [dbo].[Role_Feature] (rid, fid) VALUES
(3, 1), -- Trang chủ
(3, 2), -- Tạo đơn
(3, 3); -- Tình trạng

-- Group Leader có tất cả quyền
INSERT INTO [dbo].[Role_Feature] (rid, fid) VALUES
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5);

-- Division Leader có tất cả quyền
INSERT INTO [dbo].[Role_Feature] (rid, fid) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);
