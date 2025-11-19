-- Create databases only if they do NOT already exist
DO
$$
BEGIN
    -- COMPANY DB
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'company') THEN
        CREATE DATABASE company;
        RAISE NOTICE 'Database company created.';
    ELSE
        RAISE NOTICE 'Database company already exists.';
    END IF;

    -- JOB DB
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'job') THEN
        CREATE DATABASE job;
        RAISE NOTICE 'Database job created.';
    ELSE
        RAISE NOTICE 'Database job already exists.';
    END IF;

    -- REVIEW DB
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'review') THEN
        CREATE DATABASE review;
        RAISE NOTICE 'Database review created.';
    ELSE
        RAISE NOTICE 'Database review already exists.';
    END IF;
END
$$;
