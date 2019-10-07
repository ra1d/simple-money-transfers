INSERT INTO PUBLIC."account" ("id", "active", "balance", "creation_time", "description", "holder_name") VALUES (1, true, 1000, '2019-02-01 01:02:03.000000000', 'Test account 1 description', 'Test Account 1');
INSERT INTO PUBLIC."account" ("id", "active", "balance", "creation_time", "description", "holder_name") VALUES (2, true, 2000, '2019-02-02 02:03:04.000000000', 'Test account 2 description', 'Test Account 2');
INSERT INTO PUBLIC."account" ("id", "active", "balance", "creation_time", "description", "holder_name") VALUES (3, false, 3000, '2019-02-03 03:04:05.000000000', 'Test account 3 description - disabled', 'Test Account 3');
INSERT INTO PUBLIC."account" ("id", "active", "balance", "creation_time", "description", "holder_name") VALUES (4, true, 4000, '2019-02-04 04:05:06.000000000', 'Test account 4 description', 'Test Account 4');

INSERT INTO PUBLIC."transfer" ("id", "amount", "creation_time", "description", "from_account", "to_account") VALUES (1, 100, '2019-02-11 10:00:01.000000000', 'Test transfer 1-2', 1, 2);
INSERT INTO PUBLIC."transfer" ("id", "amount", "creation_time", "description", "from_account", "to_account") VALUES (2, 200, '2019-02-12 10:00:02.000000000', 'Test transfer 2-3', 2, 3);
INSERT INTO PUBLIC."transfer" ("id", "amount", "creation_time", "description", "from_account", "to_account") VALUES (3, 300, '2019-02-13 10:00:03.000000000', 'Test transfer 3-4', 3, 4);
INSERT INTO PUBLIC."transfer" ("id", "amount", "creation_time", "description", "from_account", "to_account") VALUES (4, 410, '2019-02-14 10:00:04.000000000', 'Test transfer 4-1', 4, 1);
INSERT INTO PUBLIC."transfer" ("id", "amount", "creation_time", "description", "from_account", "to_account") VALUES (5, 420, '2019-02-15 10:00:05.000000000', 'Test transfer 4-2', 4, 2);
