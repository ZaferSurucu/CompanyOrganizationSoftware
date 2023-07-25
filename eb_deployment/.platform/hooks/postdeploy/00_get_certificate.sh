#!/usr/bin/env bash


if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d delta.eu-west-1.elasticbeanstalk.com --nginx --agree-tos --email scream_boy.s@hotmail.com
fi
