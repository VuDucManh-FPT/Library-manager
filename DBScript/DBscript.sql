USE [master]
GO
/****** Object:  Database [Library]    Script Date: 9/18/2024 10:58:25 AM ******/
CREATE DATABASE [Library]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Library', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.MSISERVER2024\MSSQL\DATA\Library.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Library_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.MSISERVER2024\MSSQL\DATA\Library_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [Library] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Library].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Library] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Library] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Library] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Library] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Library] SET ARITHABORT OFF 
GO
ALTER DATABASE [Library] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Library] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Library] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Library] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Library] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Library] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Library] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Library] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Library] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Library] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Library] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Library] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Library] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Library] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Library] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Library] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Library] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Library] SET RECOVERY FULL 
GO
ALTER DATABASE [Library] SET  MULTI_USER 
GO
ALTER DATABASE [Library] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Library] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Library] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Library] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Library] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Library] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'Library', N'ON'
GO
ALTER DATABASE [Library] SET QUERY_STORE = OFF
GO
USE [Library]
GO
/****** Object:  Table [dbo].[AccountState]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[AccountState](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[note] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_AccountState] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Author]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Author](
	[id] [int] NOT NULL,
	[name] [nvarchar](255) NULL,
	[description] [nvarchar](255) NULL,
 CONSTRAINT [PK_Author] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BaseSalary]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BaseSalary](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[staffID] [int] NOT NULL,
	[startDate] [date] NOT NULL,
	[baseSalary] [int] NOT NULL,
 CONSTRAINT [PK_BaseSalary] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Book]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Book](
	[id] [int] NOT NULL,
	[name] [int] NOT NULL,
	[description] [nvarchar](max) NULL,
	[publisherID] [int] NOT NULL,
 CONSTRAINT [PK_Book] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Book_Author]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Book_Author](
	[bookID] [int] NOT NULL,
	[authorID] [int] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Book_BookStoringArea]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Book_BookStoringArea](
	[bookID] [int] NOT NULL,
	[bookStoringAreaID] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[section] [nchar](10) NOT NULL,
 CONSTRAINT [PK_Book_BookStoringArea] PRIMARY KEY CLUSTERED 
(
	[bookID] ASC,
	[bookStoringAreaID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookCondition]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookCondition](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[detail] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_BookCondition] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookFeedback]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookFeedback](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[feedbackDate] [date] NOT NULL,
	[detail] [nvarchar](255) NOT NULL,
	[rating] [int] NULL,
	[borrowIndexID] [int] NULL,
 CONSTRAINT [PK_BookFeedback] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookImage]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookImage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[bookID] [int] NOT NULL,
	[imageURL] [varchar](255) NOT NULL,
 CONSTRAINT [PK_BookImage] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookImport]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookImport](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[importDate] [date] NULL,
	[isHanlded] [bit] NOT NULL,
	[providerID] [int] NOT NULL,
 CONSTRAINT [PK_BookImport] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookStoringArea]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookStoringArea](
	[id] [int] NOT NULL,
	[name] [nvarchar](255) NULL,
	[description] [nvarchar](255) NULL,
 CONSTRAINT [PK_BookStoringArea] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BorrowFine]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BorrowFine](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[borrowIndexID] [int] NOT NULL,
	[reason] [nvarchar](255) NOT NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_BorrowFine] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BorrowIndex]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BorrowIndex](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[studentID] [int] NOT NULL,
	[staffID] [int] NOT NULL,
	[bookID] [int] NOT NULL,
	[bookConditionBefore] [int] NOT NULL,
	[bookConditionAfter] [int] NOT NULL,
	[borrowDate] [date] NOT NULL,
	[estimatedReturnDate] [date] NOT NULL,
	[returnDate] [date] NULL,
 CONSTRAINT [PK_BorrowIndex] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Genre]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Genre](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NULL,
	[description] [nvarchar](255) NULL,
 CONSTRAINT [PK_Genre] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Genre_Book]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Genre_Book](
	[gerneID] [int] NOT NULL,
	[bookID] [int] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImportDetail]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImportDetail](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[bookImportID] [int] NOT NULL,
	[bookID] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [int] NOT NULL,
	[vat] [float] NULL,
 CONSTRAINT [PK_ImportDetail] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MembershipInfo]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MembershipInfo](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_MembershipInfo] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MembershipPayment]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MembershipPayment](
	[id] [int] NOT NULL,
	[membershipID] [int] NOT NULL,
	[duration] [varchar](255) NOT NULL,
	[price] [int] NOT NULL,
 CONSTRAINT [PK_MembershipPayment] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Provider]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Provider](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NULL,
	[taxCode] [nchar](15) NOT NULL,
	[note] [nvarchar](255) NULL,
 CONSTRAINT [PK_Provider] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Publisher]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Publisher](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NULL,
	[note] [nvarchar](255) NULL,
 CONSTRAINT [PK_Publisher] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Role]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Role](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_Role] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Salary]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Salary](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[staffID] [int] NOT NULL,
	[date] [date] NOT NULL,
	[salary] [int] NOT NULL,
	[paymentDate] [nchar](10) NULL,
 CONSTRAINT [PK_Salary] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SalaryChange]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SalaryChange](
	[id] [int] NOT NULL,
	[staffID] [int] NOT NULL,
	[reason] [nvarchar](255) NOT NULL,
	[value] [int] NOT NULL,
 CONSTRAINT [PK_SalaryChange] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Staff]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Staff](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[email] [nvarchar](255) NOT NULL,
	[password] [nvarchar](255) NOT NULL,
	[roleID] [int] NULL,
	[stateID] [int] NOT NULL,
 CONSTRAINT [PK_Staff] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Student]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Student](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[email] [nvarchar](255) NOT NULL,
	[password] [nvarchar](255) NOT NULL,
	[stateID] [int] NOT NULL,
 CONSTRAINT [PK_Student] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Subscription]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Subscription](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[studentID] [int] NOT NULL,
	[membershipID] [int] NOT NULL,
	[startDate] [date] NOT NULL,
	[endDate] [date] NOT NULL,
 CONSTRAINT [PK_Subscription] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[WorkingSchedule]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[WorkingSchedule](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[workingShiftID] [int] NOT NULL,
	[workingDate] [date] NOT NULL,
	[staffID] [int] NOT NULL,
 CONSTRAINT [PK_WorkingSchedule] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[WorkingShift]    Script Date: 9/18/2024 10:58:26 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[WorkingShift](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[startTime] [time](7) NOT NULL,
	[endTime] [time](7) NOT NULL,
 CONSTRAINT [PK_WorkingShift] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BaseSalary]  WITH CHECK ADD  CONSTRAINT [FK_BaseSalary_Staff] FOREIGN KEY([staffID])
REFERENCES [dbo].[Staff] ([id])
GO
ALTER TABLE [dbo].[BaseSalary] CHECK CONSTRAINT [FK_BaseSalary_Staff]
GO
ALTER TABLE [dbo].[Book]  WITH CHECK ADD  CONSTRAINT [FK_Book_Publisher] FOREIGN KEY([publisherID])
REFERENCES [dbo].[Publisher] ([id])
GO
ALTER TABLE [dbo].[Book] CHECK CONSTRAINT [FK_Book_Publisher]
GO
ALTER TABLE [dbo].[Book_Author]  WITH CHECK ADD  CONSTRAINT [FK_Book_Author_Author] FOREIGN KEY([authorID])
REFERENCES [dbo].[Author] ([id])
GO
ALTER TABLE [dbo].[Book_Author] CHECK CONSTRAINT [FK_Book_Author_Author]
GO
ALTER TABLE [dbo].[Book_Author]  WITH CHECK ADD  CONSTRAINT [FK_Book_Author_Book] FOREIGN KEY([bookID])
REFERENCES [dbo].[Book] ([id])
GO
ALTER TABLE [dbo].[Book_Author] CHECK CONSTRAINT [FK_Book_Author_Book]
GO
ALTER TABLE [dbo].[Book_BookStoringArea]  WITH CHECK ADD  CONSTRAINT [FK_Book_BookStoringArea_Book] FOREIGN KEY([bookID])
REFERENCES [dbo].[Book] ([id])
GO
ALTER TABLE [dbo].[Book_BookStoringArea] CHECK CONSTRAINT [FK_Book_BookStoringArea_Book]
GO
ALTER TABLE [dbo].[Book_BookStoringArea]  WITH CHECK ADD  CONSTRAINT [FK_Book_BookStoringArea_BookStoringArea] FOREIGN KEY([bookStoringAreaID])
REFERENCES [dbo].[BookStoringArea] ([id])
GO
ALTER TABLE [dbo].[Book_BookStoringArea] CHECK CONSTRAINT [FK_Book_BookStoringArea_BookStoringArea]
GO
ALTER TABLE [dbo].[BookFeedback]  WITH CHECK ADD  CONSTRAINT [FK_BookFeedback_BorrowIndex] FOREIGN KEY([borrowIndexID])
REFERENCES [dbo].[BorrowIndex] ([id])
GO
ALTER TABLE [dbo].[BookFeedback] CHECK CONSTRAINT [FK_BookFeedback_BorrowIndex]
GO
ALTER TABLE [dbo].[BookImage]  WITH CHECK ADD  CONSTRAINT [FK_BookImage_Book] FOREIGN KEY([bookID])
REFERENCES [dbo].[Book] ([id])
GO
ALTER TABLE [dbo].[BookImage] CHECK CONSTRAINT [FK_BookImage_Book]
GO
ALTER TABLE [dbo].[BookImport]  WITH CHECK ADD  CONSTRAINT [FK_BookImport_Provider] FOREIGN KEY([providerID])
REFERENCES [dbo].[Provider] ([id])
GO
ALTER TABLE [dbo].[BookImport] CHECK CONSTRAINT [FK_BookImport_Provider]
GO
ALTER TABLE [dbo].[BorrowFine]  WITH CHECK ADD  CONSTRAINT [FK_BorrowFine_BorrowIndex] FOREIGN KEY([borrowIndexID])
REFERENCES [dbo].[BorrowIndex] ([id])
GO
ALTER TABLE [dbo].[BorrowFine] CHECK CONSTRAINT [FK_BorrowFine_BorrowIndex]
GO
ALTER TABLE [dbo].[BorrowIndex]  WITH CHECK ADD  CONSTRAINT [FK_BorrowIndex_BookCondition] FOREIGN KEY([bookConditionBefore])
REFERENCES [dbo].[BookCondition] ([id])
GO
ALTER TABLE [dbo].[BorrowIndex] CHECK CONSTRAINT [FK_BorrowIndex_BookCondition]
GO
ALTER TABLE [dbo].[BorrowIndex]  WITH CHECK ADD  CONSTRAINT [FK_BorrowIndex_BookCondition1] FOREIGN KEY([bookConditionAfter])
REFERENCES [dbo].[BookCondition] ([id])
GO
ALTER TABLE [dbo].[BorrowIndex] CHECK CONSTRAINT [FK_BorrowIndex_BookCondition1]
GO
ALTER TABLE [dbo].[BorrowIndex]  WITH CHECK ADD  CONSTRAINT [FK_BorrowIndex_Staff] FOREIGN KEY([staffID])
REFERENCES [dbo].[Staff] ([id])
GO
ALTER TABLE [dbo].[BorrowIndex] CHECK CONSTRAINT [FK_BorrowIndex_Staff]
GO
ALTER TABLE [dbo].[BorrowIndex]  WITH CHECK ADD  CONSTRAINT [FK_BorrowIndex_Student] FOREIGN KEY([studentID])
REFERENCES [dbo].[Student] ([id])
GO
ALTER TABLE [dbo].[BorrowIndex] CHECK CONSTRAINT [FK_BorrowIndex_Student]
GO
ALTER TABLE [dbo].[Genre_Book]  WITH CHECK ADD  CONSTRAINT [FK_Genre_Book_Book] FOREIGN KEY([bookID])
REFERENCES [dbo].[Book] ([id])
GO
ALTER TABLE [dbo].[Genre_Book] CHECK CONSTRAINT [FK_Genre_Book_Book]
GO
ALTER TABLE [dbo].[Genre_Book]  WITH CHECK ADD  CONSTRAINT [FK_Genre_Book_Genre] FOREIGN KEY([gerneID])
REFERENCES [dbo].[Genre] ([id])
GO
ALTER TABLE [dbo].[Genre_Book] CHECK CONSTRAINT [FK_Genre_Book_Genre]
GO
ALTER TABLE [dbo].[ImportDetail]  WITH CHECK ADD  CONSTRAINT [FK_ImportDetail_Book] FOREIGN KEY([bookID])
REFERENCES [dbo].[Book] ([id])
GO
ALTER TABLE [dbo].[ImportDetail] CHECK CONSTRAINT [FK_ImportDetail_Book]
GO
ALTER TABLE [dbo].[ImportDetail]  WITH CHECK ADD  CONSTRAINT [FK_ImportDetail_BookImport] FOREIGN KEY([bookImportID])
REFERENCES [dbo].[BookImport] ([id])
GO
ALTER TABLE [dbo].[ImportDetail] CHECK CONSTRAINT [FK_ImportDetail_BookImport]
GO
ALTER TABLE [dbo].[MembershipPayment]  WITH CHECK ADD  CONSTRAINT [FK_MembershipPayment_MembershipInfo] FOREIGN KEY([membershipID])
REFERENCES [dbo].[MembershipInfo] ([id])
GO
ALTER TABLE [dbo].[MembershipPayment] CHECK CONSTRAINT [FK_MembershipPayment_MembershipInfo]
GO
ALTER TABLE [dbo].[Salary]  WITH CHECK ADD  CONSTRAINT [FK_Salary_Staff] FOREIGN KEY([staffID])
REFERENCES [dbo].[Staff] ([id])
GO
ALTER TABLE [dbo].[Salary] CHECK CONSTRAINT [FK_Salary_Staff]
GO
ALTER TABLE [dbo].[SalaryChange]  WITH CHECK ADD  CONSTRAINT [FK_SalaryChange_Staff] FOREIGN KEY([staffID])
REFERENCES [dbo].[Staff] ([id])
GO
ALTER TABLE [dbo].[SalaryChange] CHECK CONSTRAINT [FK_SalaryChange_Staff]
GO
ALTER TABLE [dbo].[Staff]  WITH CHECK ADD  CONSTRAINT [FK_Staff_AccountState] FOREIGN KEY([stateID])
REFERENCES [dbo].[AccountState] ([id])
GO
ALTER TABLE [dbo].[Staff] CHECK CONSTRAINT [FK_Staff_AccountState]
GO
ALTER TABLE [dbo].[Staff]  WITH CHECK ADD  CONSTRAINT [FK_Staff_Role] FOREIGN KEY([roleID])
REFERENCES [dbo].[Role] ([id])
GO
ALTER TABLE [dbo].[Staff] CHECK CONSTRAINT [FK_Staff_Role]
GO
ALTER TABLE [dbo].[Student]  WITH CHECK ADD  CONSTRAINT [FK_Student_AccountState] FOREIGN KEY([stateID])
REFERENCES [dbo].[AccountState] ([id])
GO
ALTER TABLE [dbo].[Student] CHECK CONSTRAINT [FK_Student_AccountState]
GO
ALTER TABLE [dbo].[Subscription]  WITH CHECK ADD  CONSTRAINT [FK_Subscription_MembershipInfo] FOREIGN KEY([membershipID])
REFERENCES [dbo].[MembershipInfo] ([id])
GO
ALTER TABLE [dbo].[Subscription] CHECK CONSTRAINT [FK_Subscription_MembershipInfo]
GO
ALTER TABLE [dbo].[Subscription]  WITH CHECK ADD  CONSTRAINT [FK_Subscription_Student] FOREIGN KEY([studentID])
REFERENCES [dbo].[Student] ([id])
GO
ALTER TABLE [dbo].[Subscription] CHECK CONSTRAINT [FK_Subscription_Student]
GO
ALTER TABLE [dbo].[WorkingSchedule]  WITH CHECK ADD  CONSTRAINT [FK_WorkingSchedule_Staff] FOREIGN KEY([staffID])
REFERENCES [dbo].[Staff] ([id])
GO
ALTER TABLE [dbo].[WorkingSchedule] CHECK CONSTRAINT [FK_WorkingSchedule_Staff]
GO
ALTER TABLE [dbo].[WorkingSchedule]  WITH CHECK ADD  CONSTRAINT [FK_WorkingSchedule_WorkingShift] FOREIGN KEY([workingShiftID])
REFERENCES [dbo].[WorkingShift] ([id])
GO
ALTER TABLE [dbo].[WorkingSchedule] CHECK CONSTRAINT [FK_WorkingSchedule_WorkingShift]
GO
USE [master]
GO
ALTER DATABASE [Library] SET  READ_WRITE 
GO
