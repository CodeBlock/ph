#!/usr/bin/env bash

cwd="$( cd "${BASH_SOURCE[0]%/*}" && pwd )"

if [ ! -f "$cwd/dist/build/ph-website/ph-website" ]; then
  echo "ph-website binary not found."
  exit 1
fi

"$cwd/dist/build/ph-website/ph-website" clean
"$cwd/dist/build/ph-website/ph-website" build

if [ ! -d "$cwd/_site" ]; then
  echo "_site not found."
  exit 1
fi

dir="$(mktemp -d)"
cp -r _site "$dir"
pushd "$dir"
git clone git@github.com:CodeBlock/ph.git
cd ph
git checkout gh-pages
shopt -s extglob
rm -rfv !(CNAME)
mv ../_site/* .
git add -A .
git commit -m "Deploy"
git push origin gh-pages
popd
rm -rfv "$dir"
