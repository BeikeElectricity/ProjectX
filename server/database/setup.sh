#!/bin/bash
# Simple script that connects to the database and runs some sqlscripts
echo -e "Please enter your mysql username:"
read user
echo -e "Please enter your mysql password:"
read -s pass
echo `mysql --user="$user" --password="$pass" < init.sql`
echo -e "That might have worked! But please check the database yourself!"
