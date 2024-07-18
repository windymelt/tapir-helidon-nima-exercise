import polars as pl
import seaborn as sns
import matplotlib.pyplot as plt

df = pl.read_parquet("http-server-chart.parquet/*.parquet")

# violin plot
sns.set_theme(style="whitegrid")
plt.figure(figsize=(10,10))
plt.yscale('log')
ax = sns.violinplot(data=df.to_pandas(), y="value", hue="file", split=True)
#ax.set(ylim=(0, 10))
ax.set(ylabel="http_req_duration [ms]", title="k6 stress test [100 VUs 30 sec]")
# save
plt.savefig("violin.png")