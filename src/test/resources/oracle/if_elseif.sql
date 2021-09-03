DECLARE
    jobid     employees.job_id%TYPE;
    empid     employees.employee_id%TYPE := 115;
    sal_raise NUMBER(3, 2);
BEGIN
    SELECT job_id INTO jobid from employees WHERE employee_id = empid;
    IF jobid = 'PU_CLERK' THEN
        sal_raise := .09;
    ELSIF jobid = 'SH_CLERK' THEN
        sal_raise := .08;
    ELSIF jobid = 'ST_CLERK' THEN sal_raise := .07;
    ELSE
        sal_raise := 0;
    END IF;
END;