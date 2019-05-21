insert into instrument_inv(instrument_id,created_by,created_on)
 values(2,'Default User',sysdate);
insert into order_book_inv(order_book_id,instrument_id,order_Book_Status,created_by,created_on)
 values(1001,2,'OPEN','Default User',sysdate);
 
 insert into instrument_inv(instrument_id,created_by,created_on)
 values(3,'Default User',sysdate);
insert into order_book_inv(order_book_id,instrument_id,order_Book_Status,created_by,created_on)
 values(1002,3,'OPEN','Default User',sysdate);
 
  insert into instrument_inv(instrument_id,created_by,created_on)
 values(4,'Default User',sysdate);
insert into order_book_inv(order_book_id,instrument_id,order_Book_Status,created_by,created_on)
 values(1003,4,'CLOSED','Default User',sysdate);
 
 
  insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity,)
 values(2004,'VALID','LIMIT_ORDER',0);
 insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2004,2004,4,20,20,'Default User',sysdate,1003);
 
   insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity,)
 values(2008,'VALID','LIMIT_ORDER',0);
 insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2008,2008,4,10,20,'Default User',sysdate,1003);
 
 insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity)
 values(2005,'VALID','LIMIT_ORDER',0); 
  insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2005,2005,4,50,50,'Default User',sysdate+1,1003);
 
 insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity)
 values(2009,'VALID','LIMIT_ORDER',0); 
  insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2009,2009,4,50,50,'Default User',sysdate+1,1003);

insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity)
 values(2006,'VALID','MARKET_ORDER',0); 
   insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2006,2006,4,100,null,'Default User',sysdate+2,1003);
 
 insert into ORDERS_DETAILS_INV(order_Details_id,order_status,order_type,execution_quantity)
 values(2007,'VALID','MARKET_ORDER',0); 
    insert into orders_inv(order_id,order_Details_id,instrument_id,order_quantity,order_price,created_by,created_on,order_book_id)
 values(2007,2007,4,30,null,'Default User',sysdate+3,1003);
 
 insert into instrument_inv(instrument_id,created_by,created_on)
 values(6,'Default User',sysdate);
insert into order_book_inv(order_book_id,instrument_id,order_Book_Status,created_by,created_on)
 values(1007,6,'OPEN','Default User',sysdate);
 
  insert into instrument_inv(instrument_id,created_by,created_on)
 values(7,'Default User',sysdate);
insert into order_book_inv(order_book_id,instrument_id,order_Book_Status,created_by,created_on)
 values(1008,7,'EXECUTED','Default User',sysdate);

 