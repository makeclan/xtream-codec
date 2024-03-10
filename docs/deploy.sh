#!/bin/bash

set -e

pnpm install
pnpm run docs:build

# 进入生成的文件夹
cd src/.vuepress/dist
#
git init
git add -A
git commit -m 'deploy docs'
#
## 如果发布到 https://<USERNAME>.github.io/<REPO>
git push -f git@github.com:hylexus/xtream-codec.git main:gh-pages
git push -f git@gitee.com:hylexus/xtream-codec.git main:gh-pages
#
cd -
