{-# LANGUAGE OverloadedStrings #-}

import Control.Monad.IO.Class
import Data.Monoid
import Data.Time.Clock.POSIX
import Hakyll

main :: IO ()
main = do
  ts <- liftIO getPOSIXTime

  hakyll $ do
    let ts' = show ts -- Do this once so miliseconds don't ever change.

    match "images/*" $ do
      route idRoute
      compile copyFileCompiler

    match "css/*" $ do
      route $ setExtension (ts' ++ ".less")
      compile compressCssCompiler

    match "templates/*" $ compile templateCompiler

    match "*.html" $ do
      let ctx = mconcat [ constField "ts" ts'
                        , defaultContext
                        ]
      route $ setExtension "html"
      compile $
        getResourceBody
          >>= loadAndApplyTemplate "templates/default.html" ctx
          >>= relativizeUrls
