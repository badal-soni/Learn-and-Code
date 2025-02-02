public class CsvCustomerExporter : ICustomerExporter {

    public string Export(List<Customer> customers) {
        StringBuilder sb = new StringBuilder();
        sb.AppendLine("CustomerID,CompanyName,ContactName,Country");

        foreach (var customer in customers) {
            sb.AppendFormat("{0},{1},{2},{3}",
                customer.CustomerID,
                customer.CompanyName,
                customer.ContactName,
                customer.Country);
            sb.AppendLine();
        }

        return sb.ToString();
    }
}
