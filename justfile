all: measure sample plot

measure:
    scala-cli run measure.scala.sc

sample:
    scala-cli run sample.scala.sc -- *.json

plot:
    python3 plot.py

clean:
    rm -rf *.json *.parquet

distclean: clean
    rm violin.png