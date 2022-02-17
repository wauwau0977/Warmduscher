#! /bin/bash


# see crontab in notes

/usr/bin/pg_dump thserverdb | gzip > /diskstation/backup/`echo $HOSTNAME`_`date +"%Y%m%d_%H%M%S"`.gz


