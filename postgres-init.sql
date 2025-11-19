-- Create COMPANY database
SELECT 'CREATE DATABASE company'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'company')
    \gexec

-- Create JOB database
SELECT 'CREATE DATABASE job'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'job')
    \gexec

-- Create REVIEW database
SELECT 'CREATE DATABASE review'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'review')
    \gexec
