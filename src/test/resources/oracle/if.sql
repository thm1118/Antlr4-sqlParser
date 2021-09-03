DECLARE
    sal_raise FLOAT ;
    job_id    NUMBER ;
begin
    if job_id = 1 then
        sal_raise := 0.6;
    end if;
end;