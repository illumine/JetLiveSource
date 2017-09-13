create table test.weather( id int primary key, city varchar(50), temp_lo int, temp_hi int, humidity real, press float, created date )

create table test.table_a( id int primary key, data_a varchar(50) )
create table test.table_b( id int primary key, data_b varchar(50) )
create table test.atob( a integer, b integer, c integer, primary key(a,b) ) 


#INFORMATION_SCHEMA
#table description
select * from INFORMATION_SCHEMA.COLUMNS  where table_name='weather' and table_schema='test' order by ordinal_position
select column_name, data_type from INFORMATION_SCHEMA.COLUMNS  where table_name='weather' and table_schema='test' order by ordinal_position


select * from information_schema.table_constraints where table_name='weather' and table_schema='test'

#primary key
select * from information_schema.key_column_usage where table_name='atob' and table_schema='test'

select column_name from information_schema.key_column_usage where table_name='weather' and table_schema='test'


#
select * from pg_class where relname = 'weather'

select * from pg_constraint where relname ='weather'

select relname from pg_class where oid in ( select indexrelid from pg_index a, pg_class b where b.relname='weather' and b.oid=a.indrelid and ( indisunique='t' or indisprimary='t'))  


#procedures
drop  function if exists test.fun_check_weather
create  or replace function  fun_check_weather1( 
in p_city varchar ,
in p_created date ,
out p_temp_h  integer,
out p_temp_l  integer,
out p_id  integer
)  as $$

BEGIN
select id, temp_lo, temp_hi into p_temp_l, p_temp_h, p_id from weather where city=p_city and id=1;


END;
$$ language plpgsql;


create  or replace function  fun_add_numbers( 
in a integer,
in b integer,
in x real,
in y real)  returns real as $$
declare
 x real;

BEGIN
 x = a + b + x + y;

 return x;


END;
$$ language plpgsql;



create  or replace function  fun_no_args( ) returns integer as $$
BEGIN
 return 1;
END;
$$ language plpgsql;


select * from pg_proc where proname in('fun_add_numbers' ,'fun_check_weather' )'fun_add_numbers'  
select * from pg_type where typarray=1082  order by typarray 





"{p_city,p_created,p_temp_h,p_temp_l}" -> proargnames
"{1043,  1082,     23,      23}"       ->   
select tp.oid, tp.* from  pg_type tp  order by  tp.oid

select * from pg_proc pr, pg_type tp
where tp.oid = pr.prorettype
and pr.proisagg=FALSE 
and tp.typname <> 'trigger'
and pr.pronamespace in (
  select oid from pg_namespace where nspname not like 'pg_%'
  and nspname != 'information_schema' );

select tp.oid, tp.* from  pg_type tp  where oid=1043
select tp.oid, tp.* from  pg_type tp  where oid=1082
select tp.oid, tp.* from  pg_type tp  where oid=23
select typname from  pg_type   where typnamespace=11 and typinput not like 'record%'

select prorettype, proargtypes, proallargtypes, proargmodes, proargnames from pg_proc pr, pg_type tp
where proname='fun_no_args'
and tp.oid = pr.prorettype
and pr.proisagg=FALSE 
and tp.typname <> 'trigger'
and pr.pronamespace in (
  select oid from pg_namespace where nspname not like 'pg_%'
  and nspname != 'information_schema' );




select * from information_schema.routines where specific_schema not in ('pg_catalog','information_schema') and type_udt_name != 'trigger'  

