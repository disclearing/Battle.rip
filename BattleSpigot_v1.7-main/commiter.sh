#!/bin/bash

set -eu
Shit1() {
    if Shit2; then
	  set -e
	  sh -c "git config --global user.name 'NulledCodeDev' \
      && git config --global user.email 'oscarfdo2004@gmail.com' \
      && git add -A && git commit -m 'Updated$(git status -s | sed 's/M//')' --allow-empty \
      && git push -u origin HEAD"
    fi
}
Shit2() {	
    [ -n "$(git status -s)" ]
}
while true
do
  Shit1
  sleep 1
  clear
done

