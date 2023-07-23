#!/usr/bin/env bash


if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d INSERT_AWS_DOMAIN --nginx --agree-tos --email INSERT_EMAIL
fi
