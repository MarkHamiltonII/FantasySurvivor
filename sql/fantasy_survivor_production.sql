drop database if exists fantasy_survivor;
create database fantasy_survivor;
use fantasy_survivor;

create table castaway (
	castaway_id int primary key auto_increment,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    age int null,
    current_residence varchar(250) null,
    occupation varchar(100) null,
    icon_url varchar(2048) not null,
    page_url varchar(2048) not null
);

insert into castaway (first_name, last_name, age, current_residence, occupation, icon_url, page_url) values
	('Chanelle', 'Howell', 29, 'Hamden, CT', 'Executive Recruiter', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_02_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216607/'),
    ('Daniel', 'Strunk', 30, 'New Haven, CT', 'Law Clerk', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_04_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216608/'),
    ('Drea', 'Wheeler', 35, 'Montreal, Quebec', 'Fitness Consultant', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_13_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216609/'),
    ('Hai', 'Giang', 29, 'New Orleans, LA', 'Data Scientist', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_05_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216610/'),
    ('Jackson', 'Fox', 48, 'Houston, TX', 'Healthcare Worker', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_10_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216611/'),
    ('Jenny', 'Kim', 43, 'Brooklyn, NY', 'Creative Director', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_01_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216612/'),
    ('Jonathan', 'Young', 29, 'Gulfshores, AL', 'Beach Service Co. Owner', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_12_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216613/'),
    ('Lindsay', 'Dolashewich', 31, 'Asbury Park, NJ', 'Dietitian', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_08_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216614/'),
    ('Lydia', 'Meredith', 22, 'Santa Monica, CA', 'Waitress', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_03_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216615/'),
    ('Marya', 'Sherron', 47, 'Noblesville, IN', 'Stay at Home Mom', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_07_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216616/'),
    ('Maryanne', 'Oketch', 24, 'Ajax, Ontario', 'Seminary Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_09_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216617/'),
    ('Mike', 'Turner', 58, 'Hoboken, NJ', 'Retired Firefighter', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_06_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216618/'),
    ('Omar', 'Zaheer', 31, 'Whitby, Ontario', 'Veterinarian', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_11_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216619/'),
    ('Rocksroy', 'Bailey', 44, 'Las Vegas, NV', 'Stay at Home Dad', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_16_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216620/'),
    ('Romeo', 'Escobar', 37, 'Norwalk, CA', 'Pageant Coach', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_17_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216621/'),
    ('Swati', 'Goel', 19, 'Palo Alto, Ca', 'Ivy League Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_15_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216622/'),
    ('Tori', 'Meehan', 25, 'Tulsa, OK', 'Therapist', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_14_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216623/'),
    ('Zach', 'Wurtenberger', 22, 'St. Louis, MO', 'Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_18_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216624/');